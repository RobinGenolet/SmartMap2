package ch.epfl.smartmap.servercom;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import ch.epfl.smartmap.cache.ImmutableEvent;
import ch.epfl.smartmap.cache.ImmutableUser;
import ch.epfl.smartmap.cache.User;
import ch.epfl.smartmap.util.Utils;

/**
 * A {@link SmartMapParser} implementation that parses objects from Json format
 * 
 * @author marion-S
 * @author SpicyCH (code reviewed 02.11.2014) : changed Error to error as the
 *         server uses a lower case.
 */
@SuppressLint("UseSparseArrays")
public class JsonSmartMapParser implements SmartMapParser {

    private static final String ERROR_STATUS = "error";
    private static final String FEEDBACK_STATUS = "feedback";

    private static final int SERVER_TIME_DIFFERENCE_THRESHHOLD = 10000;

    private static final int DATETIME_FORMAT_PARTS = 2;
    private static final int DATE_FORMAT_PARTS = 3;
    private static final int TIME_FORMAT_PARTS = 3;

    private static final int MAX_MONTHS_NUMBER = 11;
    private static final int MAX_DAYS_NUMBER = 31;
    private static final int MAX_HOURS_NUMBER = 23;
    private static final int MAX_MINUTES_NUMBER = 59;
    private static final int MAX_SECONDS_NUMBER = 59;

    private static final int UNITIALIZED_LATITUDE = -200;
    private static final int UNITIALIZED_LONGITUDE = -200;
    private static final int MIN_LATITUDE = -90;
    private static final int MAX_LATITUDE = 90;
    private static final int MIN_LONGITUDE = -180;
    private static final int MAX_LONGITUDE = 180;
    private static final int MAX_NAME_LENGTH = 60;
    private static final int MAX_EVENT_DESCRIPTION_LENGTH = 255;

    // private static final String TAG = "JSON_PARSER";

    /*
     * (non-Javadoc)
     * @see
     * ch.epfl.smartmap.servercom.SmartMapParser#checkServerError(java.lang.
     * String)
     */
    @Override
    public void checkServerError(String s) throws SmartMapParseException, SmartMapClientException {

        String status = null;
        String message = null;
        try {
            JSONObject jsonObject = new JSONObject(s);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }
        if (status.equals(ERROR_STATUS)) {
            throw new SmartMapClientException(message);
        }
        if (status.equals(FEEDBACK_STATUS)) {
            throw new ServerFeedbackException(message);
        }
    }

    @Override
    public ImmutableEvent parseEvent(String s) throws SmartMapParseException {
        JSONObject jsonObject = null;
        JSONObject eventJsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            eventJsonObject = jsonObject.getJSONObject("event");
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }
        return this.parseEventFromJSON(eventJsonObject);
    }

    @Override
    public List<ImmutableEvent> parseEventList(String s) throws SmartMapParseException {
        List<ImmutableEvent> events = new ArrayList<ImmutableEvent>();

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray eventsArray = jsonObject.getJSONArray("events");

            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventJSON = eventsArray.getJSONObject(i);
                ImmutableEvent event = this.parseEventFromJSON(eventJSON);
                events.add(event);
                Log.d("events", event.getName());
            }
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }

        return events;
    }

    /*
     * (non-Javadoc)
     * @see
     * ch.epfl.smartmap.servercom.SmartMapParser#parseFriend(java.lang.String)
     */
    @Override
    public ImmutableUser parseFriend(String s) throws SmartMapParseException {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }
        return this.parseFriendFromJSON(jsonObject);
    }

    /*
     * (non-Javadoc)
     * @see
     * ch.epfl.smartmap.servercom.SmartMapParser#parseFriends(java.lang.String)
     */
    @Override
    public List<ImmutableUser> parseFriends(String s, String key) throws SmartMapParseException {

        List<ImmutableUser> friends = new ArrayList<ImmutableUser>();

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray usersArray = jsonObject.getJSONArray(key);

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJSON = usersArray.getJSONObject(i);
                ImmutableUser friend = this.parseFriendFromJSON(userJSON);
                friends.add(friend);
            }
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }

        return friends;
    }

    /*
     * (non-Javadoc)
     * @see ch.epfl.smartmap.servercom.SmartMapParser#parseId(java.lang.String)
     */
    @Override
    public Long parseId(String s) throws SmartMapParseException {
        long id = -1;
        try {
            JSONObject jsonObject = new JSONObject(s);
            id = jsonObject.getLong("id");

        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }
        this.checkId(id);

        return id;
    }

    /*
     * (non-Javadoc)
     * @see ch.epfl.smartmap.servercom.SmartMapParser#parseIds(java.lang.String)
     */
    @Override
    public List<Long> parseIds(String s, String key) throws SmartMapParseException {
        List<Long> ids = new ArrayList<Long>();

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray idsArray = jsonObject.getJSONArray(key);

            for (int i = 0; i < idsArray.length(); i++) {
                long id = idsArray.getLong(i);
                this.checkId(id);
                ids.add(id);
            }

        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }
        return ids;
    }

    @Override
    public List<ImmutableUser> parsePositions(String s) throws SmartMapParseException {

        List<ImmutableUser> users = new ArrayList<ImmutableUser>();

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray usersArray = jsonObject.getJSONArray("positions");

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject position = usersArray.getJSONObject(i);
                long userId = position.getLong("id");
                double latitude = position.getDouble("latitude");
                double longitude = position.getDouble("longitude");
                GregorianCalendar lastSeen = this.parseDate(position.getString("lastUpdate"));

                this.checkId(userId);
                this.checkLatitude(latitude);
                this.checkLongitude(longitude);
                // this.checkLastSeen(lastSeen);

                Location location = new Location("SmartMapServers");
                location.setTime(lastSeen.getTimeInMillis());
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                String locationString = Utils.getCityFromLocation(location);

                ImmutableUser user =
                    new ImmutableUser(userId, null, null, null, location, locationString, null, false, -1);

                users.add(user);
            }
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }

        return users;
    }

    /**
     * Checks if the email address is valid
     * 
     * @param email
     * @throws SmartMapParseException
     *             if invalid email address
     */
    private void checkEmail(String email) throws SmartMapParseException {
        // TODO
    }

    private void checkEventDescription(String description) throws SmartMapParseException {
        if (description.length() > MAX_EVENT_DESCRIPTION_LENGTH) {
            throw new SmartMapParseException("Description must not be longer than 255 characters");
        }
    }

    /**
     * Checks if the id is valid
     * 
     * @param id
     * @throws SmartMapParseException
     *             if invalid id
     */
    private void checkId(long id) throws SmartMapParseException {
        if (id <= 0) {
            throw new SmartMapParseException("invalid id");
        }
    }

    /**
     * Checks if the parameter lastSeen is valid
     * 
     * @param lastSeen
     * @throws SmartMapParseException
     */
    private void checkLastSeen(GregorianCalendar lastSeen) throws SmartMapParseException {
        GregorianCalendar now = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
        if (lastSeen.getTimeInMillis() > (now.getTimeInMillis() + SERVER_TIME_DIFFERENCE_THRESHHOLD)) {
            throw new SmartMapParseException("Invalid last seen date: " + lastSeen.toString()
                + " compared to " + now.toString());
        }
    }

    /**
     * Checks if the latitude is valid
     * 
     * @param latitude
     * @throws SmartMapParseException
     *             if invalid latitude
     */
    private void checkLatitude(double latitude) throws SmartMapParseException {
        if (!((MIN_LATITUDE <= latitude) && (latitude <= MAX_LATITUDE))) {
            throw new SmartMapParseException("invalid latitude");
        }
    }

    /**
     * Checks if the longitude is valid
     * 
     * @param longitude
     * @throws SmartMapParseException
     *             if invalid longitude
     */
    private void checkLongitude(double longitude) throws SmartMapParseException {
        if (!((MIN_LONGITUDE <= longitude) && (longitude <= MAX_LONGITUDE))) {
            throw new SmartMapParseException("invalid longitude");
        }
    }

    /**
     * Checks if the name is valid
     * 
     * @param name
     * @throws SmartMapParseException
     *             if invalid name
     */
    private void checkName(String name) throws SmartMapParseException {
        if ((name.length() >= MAX_NAME_LENGTH) || (name.length() < 2)) {
            throw new SmartMapParseException("invalid name : must be between 2 and 60 characters");
        }
    }

    /**
     * Checks if the phone number is valid
     * 
     * @param phoneNumber
     * @throws SmartMapParseException
     *             if invalid phone number
     */
    private void checkPhoneNumber(String phoneNumber) throws SmartMapParseException {
        // TODO
    }

    private void checkStartingAndEndDate(GregorianCalendar startingDate, GregorianCalendar endDate)
        throws SmartMapParseException {
        if (!startingDate.before(endDate)) {
            throw new SmartMapParseException("Starting date must be before end date");
        }
    }

    /**
     * Transforms a date in format YYYY-MM-DD hh:mm:ss into a GregorianCalendar
     * instance.
     * 
     * @author Pamoi
     * @param date
     * @return
     * @throws SmartMapClientException
     */
    private GregorianCalendar parseDate(String date) throws SmartMapParseException {

        String[] dateTime = date.split(" ");

        if (dateTime.length != DATETIME_FORMAT_PARTS) {
            throw new SmartMapParseException("Invalid datetime format !");
        }

        String[] datePart = dateTime[0].split("-");

        if (datePart.length != DATE_FORMAT_PARTS) {
            throw new SmartMapParseException("Invalid date format !");
        }

        String[] timePart = dateTime[1].split(":");

        if (timePart.length != TIME_FORMAT_PARTS) {
            throw new SmartMapParseException("Invalid time format !");
        }

        int year = Integer.parseInt(datePart[0]);
        // GregorianCalendar counts months from 0.
        int month = Integer.parseInt(datePart[1]) - 1;
        int day = Integer.parseInt(datePart[2]);

        int hour = Integer.parseInt(timePart[0]);
        int minutes = Integer.parseInt(timePart[1]);
        int seconds = Integer.parseInt(timePart[2]);

        // As GregorianCalendar does not check arguments, we need to to it.
        if ((month > MAX_MONTHS_NUMBER) || (month < 0)) {
            throw new SmartMapParseException("Invalid month number !");
        }

        if ((day > MAX_DAYS_NUMBER) || (day < 1)) {
            throw new SmartMapParseException("Invalid day number !");
        }

        if ((hour > MAX_HOURS_NUMBER) || (hour < 0)) {
            throw new SmartMapParseException("Invalid hour number !");
        }

        if ((minutes > MAX_MINUTES_NUMBER) || (hour < 0)) {
            throw new SmartMapParseException("Invalid minute number !");
        }

        if ((seconds > MAX_SECONDS_NUMBER) || (seconds < 0)) {
            throw new SmartMapParseException("Invalid second number !");
        }

        // Server time is in GMT+01:00
        GregorianCalendar g = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));

        g.set(year, month, day, hour, minutes, seconds);

        return g;
    }

    private ImmutableEvent parseEventFromJSON(JSONObject jsonObject) throws SmartMapParseException {
        long id = -1;
        long creatorId = -1;
        GregorianCalendar startingDate = null;
        GregorianCalendar endDate = null;
        double latitude = UNITIALIZED_LATITUDE;
        double longitude = UNITIALIZED_LONGITUDE;
        String positionName = null;
        String name = null;
        String description = "";
        List<Long> participants;

        try {
            id = jsonObject.getLong("id");
            creatorId = jsonObject.getLong("creatorId");
            startingDate = this.parseDate(jsonObject.getString("startingDate"));
            endDate = this.parseDate(jsonObject.getString("endingDate"));
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");
            positionName = jsonObject.getString("positionName");
            name = jsonObject.getString("name");
            description = jsonObject.getString("description");
            participants = this.parseIds(jsonObject.toString(), "participants");
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }

        this.checkId(id);
        this.checkId(creatorId);
        this.checkStartingAndEndDate(startingDate, endDate);
        this.checkLatitude(latitude);
        this.checkLongitude(longitude);
        this.checkName(positionName);
        this.checkName(name);
        this.checkEventDescription(description);
        Location location = new Location("SmartMapServers");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        // Contains only id of creator to be retrieved later
        ImmutableUser creator = new ImmutableUser(creatorId, null, null, null, null, null, null, false, -1);

        ImmutableEvent event =
            new ImmutableEvent(id, name, creator, description, startingDate, endDate, location, positionName,
                new HashSet<Long>());

        return event;
    }

    /**
     * Return the friend parsed from a jsonObject
     * 
     * @param jsonObject
     * @return a friend
     * @throws SmartMapParseException
     */
    private ImmutableUser parseFriendFromJSON(JSONObject jsonObject) throws SmartMapParseException {
        long id = 0;
        String name = null;
        String phoneNumber = User.NO_PHONE_NUMBER;
        String email = User.NO_EMAIL;
        double latitude = UNITIALIZED_LATITUDE;
        double longitude = UNITIALIZED_LONGITUDE;
        String lastSeenString = null;

        try {
            id = jsonObject.getLong("id");
            name = jsonObject.getString("name");
            phoneNumber = jsonObject.optString("phoneNumber", null);
            email = jsonObject.optString("email", null);
            latitude = jsonObject.optDouble("latitude");
            longitude = jsonObject.optDouble("longitude");
            lastSeenString = jsonObject.optString("lastUpdate", null);
        } catch (JSONException e) {
            throw new SmartMapParseException(e);
        }

        this.checkId(id);
        this.checkName(name);

        Location lastSeen = null;

        if (phoneNumber != null) {
            this.checkPhoneNumber(phoneNumber);
        }
        if (email != null) {
            this.checkEmail(email);
        }
        // We do not want a location if it has not a latitude, longitude and
        // last seen date.
        if ((latitude != Double.NaN) && (longitude != Double.NaN) && (lastSeenString != null)) {
            lastSeen = new Location("SmartMapServers");
            lastSeen.setLatitude(latitude);
            lastSeen.setLongitude(longitude);
            lastSeen.setTime(this.parseDate(lastSeenString).getTimeInMillis());
        }
        return new ImmutableUser(id, name, phoneNumber, email, null, null, null, false, -1);
    }
}