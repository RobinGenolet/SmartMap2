// package ch.epfl.smartmap.test.severcom;
//
// import java.util.GregorianCalendar;
// import java.util.List;
// import java.util.TimeZone;
//
// import org.junit.Test;
//
// import android.annotation.SuppressLint;
// import android.location.Location;
// import android.test.AndroidTestCase;
// import ch.epfl.smartmap.cache.ImmutableEvent;
// import ch.epfl.smartmap.cache.ImmutableUser;
// import ch.epfl.smartmap.servercom.JsonSmartMapParser;
// import ch.epfl.smartmap.servercom.SmartMapClientException;
// import ch.epfl.smartmap.servercom.SmartMapParseException;
// import ch.epfl.smartmap.servercom.SmartMapParser;
// import ch.epfl.smartmap.util.Utils;
//
// /**
// * Tests whether the app correctly handles proper JSON
// *
// * @author marion-S
// **/
// @SuppressLint("UseSparseArrays")
// public class ProperJSONParsingTest extends AndroidTestCase {
//
<<<<<<< HEAD
// private static final String PROPER_FRIEND_JSON = "{\n" + " \"id\" : \"13\", \n"
=======
// private static final String PROPER_FRIEND_JSON = "{\n" +
// " \"id\" : \"13\", \n"
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// + " \"name\" : \"Georges\", \n" + " \"email\" : \"georges@gmail.com\", \n"
// + " \"latitude\" : \"20.03\", \n" + " \"longitude\" : \"26.85\", \n"
// + " \"lastUpdate\" : \"2014-12-03 20:21:22\", \n"
// + " \"phoneNumber\" : \"0782678654\" \n" + "}\n";
//
<<<<<<< HEAD
// private static final String PROPER_SUCCESS_STATUS_JSON = "{\n" + " \"status\" : \"Ok\", \n"
// + " \"message\" : \"Success!\" \n" + "}\n";
//
// private static final String PROPER_ERROR_STATUS_JSON = "{\n" + " \"status\" : \"error\", \n"
// + " \"message\" : \"wrong parameters\" \n" + "}\n";
//
// private static final String PROPER_FRIEND_LIST_JSON = "{\n" + " \"list\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"name\" : \"Georges\" \n" + "},\n" + "{\n" + " \"id\" : \"18\", \n"
// + " \"name\" : \"Alice\" \n" + "}\n" + "  ]\n" + "}\n";
//
// private static final String PROPER_POSITIONS_LIST_JSON = "{\n" + " \"positions\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"latitude\" : \"20.03\", \n" + " \"longitude\" : \"26.85\", \n"
// + "\"lastUpdate\": \"2014-11-12 23:54:22\"" + "},\n" + "{\n" + " \"id\" : \"18\", \n"
=======
// private static final String PROPER_SUCCESS_STATUS_JSON = "{\n" +
// " \"status\" : \"Ok\", \n"
// + " \"message\" : \"Success!\" \n" + "}\n";
//
// private static final String PROPER_ERROR_STATUS_JSON = "{\n" +
// " \"status\" : \"error\", \n"
// + " \"message\" : \"wrong parameters\" \n" + "}\n";
//
// private static final String PROPER_FRIEND_LIST_JSON = "{\n" +
// " \"list\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"name\" : \"Georges\" \n" + "},\n" + "{\n" +
// " \"id\" : \"18\", \n"
// + " \"name\" : \"Alice\" \n" + "}\n" + "  ]\n" + "}\n";
//
// private static final String PROPER_POSITIONS_LIST_JSON = "{\n" +
// " \"positions\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"latitude\" : \"20.03\", \n" +
// " \"longitude\" : \"26.85\", \n"
// + "\"lastUpdate\": \"2014-11-12 23:54:22\"" + "},\n" + "{\n" +
// " \"id\" : \"18\", \n"
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// + " \"latitude\" : \"40.0\", \n" + " \"longitude\" : \"3.0\", \n"
// + "\"lastUpdate\": \"2014-10-23 05:07:54\"" + "}\n" + "  ]\n" + "}\n";
//
// private static final String PROPER_ID_JSON = "{\n" + " \"id\" : 3" + "}\n";
//
<<<<<<< HEAD
// private static final String PROPER_ID_LIST_JSON = "{\n" + " \"friends\" : [ 3, 4, 5 ]" + "}\n";
//
// private static final String PROPER_FRIEND_EMPTY_LIST_JSON = "{\n" + " \"list\" : [\n" + "  ]\n" +
// "}\n";
//
// private static final String PROPER_POSITIONS_EMPTY_LIST_JSON = "{\n" + " \"positions\" : [\n" + "  ]\n"
// + "}\n";
//
// private static final String PROPER_IDS_EMPTY_LIST_JSON = "{\n" + " \"friends\" : [ ]" + "}\n";
//
// private static final String PROPER_EVENT_JSON = "{\n" + "\"event\":" + "{\n" + " \"id\" : \"13\", \n"
// + " \"creatorId\" : \"3\", \n" + " \"startingDate\" : \"2014-10-23 05:07:54\", \n"
// + " \"endingDate\" : \"2014-11-12 23:54:22\", \n" + " \"longitude\" : \"26.85\", \n"
// + " \"latitude\" : \"20.03\", \n" + " \"name\" : \"MyEvent\", \n"
// + " \"description\" : \"description\", \n" + " \"participants\" : [\n 3, 4, 1 \n] , \n"
// + " \"positionName\" : \"Tokyo\" \n" + "}\n" + "}\n";
//
// private static final String PROPER_EVENT_LIST_JSON = "{\n" + " \"events\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"creatorId\" : \"3\", \n"
// + " \"startingDate\" : \"2014-10-23 05:07:54\", \n" + " \"endingDate\" : \"2014-11-12 23:54:22\", \n"
// + " \"longitude\" : \"26.85\", \n" + " \"latitude\" : \"20.03\", \n"
// + " \"participants\" : [\n 3, 4, 1 \n] , \n" + " \"name\" : \"MyEvent\", \n"
// + " \"description\" : \"description\", \n" + " \"positionName\" : \"Tokyo\" \n" + "},\n" + "{\n"
// + " \"id\" : \"11\", \n" + " \"creatorId\" : \"1\", \n"
// + " \"startingDate\" : \"2015-01-02 06:10:54\", \n" + " \"endingDate\" : \"2015-02-02 22:00:11\", \n"
// + " \"latitude\" : \"40.0\", \n" + " \"longitude\" : \"3.0\", \n"
// + " \"participants\" : [\n 1 \n] , \n" + " \"name\" : \"YourEvent\", \n"
// + " \"description\" : \"description2\", \n" + " \"positionName\" : \"London\" \n" + "}\n" + "  ]\n"
// + "}\n";
//
// private static final String PROPER_EVENTS_EMPTY_LIST_JSON = "{\n" + " \"events\" : [ ]" + "}\n";
=======
// private static final String PROPER_ID_LIST_JSON = "{\n" +
// " \"friends\" : [ 3, 4, 5 ]" + "}\n";
//
// private static final String PROPER_FRIEND_EMPTY_LIST_JSON = "{\n" +
// " \"list\" : [\n" + "  ]\n" +
// "}\n";
//
// private static final String PROPER_POSITIONS_EMPTY_LIST_JSON = "{\n" +
// " \"positions\" : [\n" + "  ]\n"
// + "}\n";
//
// private static final String PROPER_IDS_EMPTY_LIST_JSON = "{\n" +
// " \"friends\" : [ ]" + "}\n";
//
// private static final String PROPER_EVENT_JSON = "{\n" + "\"event\":" + "{\n"
// + " \"id\" : \"13\", \n"
// + " \"creatorId\" : \"3\", \n" +
// " \"startingDate\" : \"2014-10-23 05:07:54\", \n"
// + " \"endingDate\" : \"2014-11-12 23:54:22\", \n" +
// " \"longitude\" : \"26.85\", \n"
// + " \"latitude\" : \"20.03\", \n" + " \"name\" : \"MyEvent\", \n"
// + " \"description\" : \"description\", \n" +
// " \"participants\" : [\n 3, 4, 1 \n] , \n"
// + " \"positionName\" : \"Tokyo\" \n" + "}\n" + "}\n";
//
// private static final String PROPER_EVENT_LIST_JSON = "{\n" +
// " \"events\" : [\n" + "{\n"
// + " \"id\" : \"13\", \n" + " \"creatorId\" : \"3\", \n"
// + " \"startingDate\" : \"2014-10-23 05:07:54\", \n" +
// " \"endingDate\" : \"2014-11-12 23:54:22\", \n"
// + " \"longitude\" : \"26.85\", \n" + " \"latitude\" : \"20.03\", \n"
// + " \"participants\" : [\n 3, 4, 1 \n] , \n" + " \"name\" : \"MyEvent\", \n"
// + " \"description\" : \"description\", \n" +
// " \"positionName\" : \"Tokyo\" \n" + "},\n" + "{\n"
// + " \"id\" : \"11\", \n" + " \"creatorId\" : \"1\", \n"
// + " \"startingDate\" : \"2015-01-02 06:10:54\", \n" +
// " \"endingDate\" : \"2015-02-02 22:00:11\", \n"
// + " \"latitude\" : \"40.0\", \n" + " \"longitude\" : \"3.0\", \n"
// + " \"participants\" : [\n 1 \n] , \n" + " \"name\" : \"YourEvent\", \n"
// + " \"description\" : \"description2\", \n" +
// " \"positionName\" : \"London\" \n" + "}\n" + "  ]\n"
// + "}\n";
//
// private static final String PROPER_EVENTS_EMPTY_LIST_JSON = "{\n" +
// " \"events\" : [ ]" + "}\n";
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
//
// private final Location location1 = new Location("SmartMapServers");
// private final Location location2 = new Location("SmartMapServers");
//
<<<<<<< HEAD
// private final GregorianCalendar date1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date2 = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date3 = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date4 = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
=======
// private final GregorianCalendar date1 = new
// GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date2 = new
// GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date3 = new
// GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
// private final GregorianCalendar date4 = new
// GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
//
// @Override
// protected void setUp() throws Exception {
// super.setUp();
//
// Utils.sContext = getContext();
//
// location1.setLatitude(20.03);
// location1.setLongitude(26.85);
//
// location2.setLatitude(40.0);
// location2.setLongitude(3.0);
//
// date1.set(2014, 10, 12, 23, 54, 22);
// date2.set(2014, 9, 23, 5, 7, 54);
// date3.set(2015, 0, 2, 6, 10, 54);
// date4.set(2015, 1, 2, 22, 00, 11);
//
// }
//
// @Test
// public void testCheckServerErrorWhenError() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// try {
// parser.checkServerError(PROPER_ERROR_STATUS_JSON);
// fail("Did not throw a SmartMapClientException whereas the server got an error");
// } catch (SmartMapClientException e) {
// // success
// } catch (Exception e) {
// e.printStackTrace();
// fail("Wrong exception thrown");
// }
// }
//
// @Test
<<<<<<< HEAD
// public void testCheckServerErrorWhenNoError() throws SmartMapParseException, SmartMapClientException {
=======
// public void testCheckServerErrorWhenNoError() throws SmartMapParseException,
// SmartMapClientException {
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// SmartMapParser parser = new JsonSmartMapParser();
// parser.checkServerError(PROPER_SUCCESS_STATUS_JSON);
// }
//
// public void testParseEvent() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// ImmutableEvent event = parser.parseEvent(PROPER_EVENT_JSON);
// this.checkEvent1(event);
// }
//
// public void testParseEventList() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// List<ImmutableEvent> events = parser.parseEventList(PROPER_EVENT_LIST_JSON);
// assertTrue("Did not parse the two pevents", events.size() == 2);
// this.checkEvent1(events.get(0));
// this.checkEvent2(events.get(1));
// }
//
// public void testParseEventListWhenEmptyList() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
<<<<<<< HEAD
// List<ImmutableEvent> events = parser.parseEventList(PROPER_EVENTS_EMPTY_LIST_JSON);
=======
// List<ImmutableEvent> events =
// parser.parseEventList(PROPER_EVENTS_EMPTY_LIST_JSON);
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// assertTrue("Did not parsed empty event list correctly", events.isEmpty());
//
// }
//
// @Test
// public void testParseFriend() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// ImmutableUser friend = parser.parseFriend(PROPER_FRIEND_JSON);
//
// assertEquals("Friend's id does not match", 13, friend.getId());
// assertEquals("Friend's name does not match", "Georges", friend.getName());
<<<<<<< HEAD
// assertEquals("Friend's email does not match", "georges@gmail.com", friend.getEmail());
// assertEquals("Friend's phone number does not match", "0782678654", friend.getPhoneNumber());
// assertEquals("Friend's latitude does not match", 20.03, friend.getLocation().getLatitude());
// assertEquals("Friend's longitude does not match", 26.85, friend.getLocation().getLongitude());
=======
// assertEquals("Friend's email does not match", "georges@gmail.com",
// friend.getEmail());
// assertEquals("Friend's phone number does not match", "0782678654",
// friend.getPhoneNumber());
// assertEquals("Friend's latitude does not match", 20.03,
// friend.getLocation().getLatitude());
// assertEquals("Friend's longitude does not match", 26.85,
// friend.getLocation().getLongitude());
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// }
//
// @Test
// public void testParseFriends() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
<<<<<<< HEAD
// List<ImmutableUser> listFriends = parser.parseFriends(PROPER_FRIEND_LIST_JSON, "list");
// assertEquals("First friend's id does not match", 13, listFriends.get(0).getId());
// assertEquals("First friend's name does not match", "Georges", listFriends.get(0).getName());
// assertEquals("Second friend's id does not match", 18, listFriends.get(1).getId());
// assertEquals("Second friend's name does not match", "Alice", listFriends.get(1).getName());
=======
// List<ImmutableUser> listFriends =
// parser.parseFriends(PROPER_FRIEND_LIST_JSON, "list");
// assertEquals("First friend's id does not match", 13,
// listFriends.get(0).getId());
// assertEquals("First friend's name does not match", "Georges",
// listFriends.get(0).getName());
// assertEquals("Second friend's id does not match", 18,
// listFriends.get(1).getId());
// assertEquals("Second friend's name does not match", "Alice",
// listFriends.get(1).getName());
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// }
//
// @Test
// public void testParseFriendsWhenEmptyList() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
<<<<<<< HEAD
// List<ImmutableUser> friends = parser.parseFriends(PROPER_FRIEND_EMPTY_LIST_JSON, "list");
=======
// List<ImmutableUser> friends =
// parser.parseFriends(PROPER_FRIEND_EMPTY_LIST_JSON, "list");
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// assertTrue("Did not parsed empty friends list correctly", friends.isEmpty());
// }
//
// public void testParseId() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// long id = parser.parseId(PROPER_ID_JSON);
// assertEquals("Id do not match", 3, id);
// }
//
// public void testParseIds() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// List<Long> ids = parser.parseIds(PROPER_ID_LIST_JSON, "friends");
// assertTrue("Did not parse the 3 ids", ids.size() == 3);
// assertEquals("First Id do not match", Long.valueOf(3), ids.get(0));
// assertEquals("Second Id do not match", Long.valueOf(4), ids.get(1));
// assertEquals("Third Id do not match", Long.valueOf(5), ids.get(2));
//
// }
//
// @Test
// public void testParseIdsWhenEmptyList() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
// List<Long> ids = parser.parseIds(PROPER_IDS_EMPTY_LIST_JSON, "friends");
// assertTrue("Did not parsed empty positions list correctly", ids.isEmpty());
// }
//
// @Test
// public void testParsePositions() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
<<<<<<< HEAD
// List<ImmutableUser> users = parser.parsePositions(PROPER_POSITIONS_LIST_JSON);
// assertTrue("Did not parse the two positions", users.size() == 2);
// assertEquals("First location's latitude does not match", location1.getLatitude(), users.get(0)
// .getLocation().getLatitude());
// assertEquals("First location's longitude does not match", location1.getLongitude(), users.get(0)
=======
// List<ImmutableUser> users =
// parser.parsePositions(PROPER_POSITIONS_LIST_JSON);
// assertTrue("Did not parse the two positions", users.size() == 2);
// assertEquals("First location's latitude does not match",
// location1.getLatitude(), users.get(0)
// .getLocation().getLatitude());
// assertEquals("First location's longitude does not match",
// location1.getLongitude(), users.get(0)
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// .getLocation().getLongitude());
// // GMT+01:00 conversion changes a few milliseconds in GregorainCalendar,
// // so we cannot test for
// // exact equality...
// assertTrue("Last seen of first user does not match",
<<<<<<< HEAD
// Math.abs(date1.getTimeInMillis() - users.get(0).getLocation().getTime()) < 1000);
// assertEquals("Second location's latitude does not match", location2.getLatitude(), users.get(1)
// .getLocation().getLatitude());
// assertEquals("Second location's longitude does not match", location2.getLongitude(), users.get(1)
// .getLocation().getLongitude());
// assertTrue("Last seen of second user does not match",
// Math.abs(date2.getTimeInMillis() - users.get(1).getLocation().getTime()) < 1000);
=======
// Math.abs(date1.getTimeInMillis() - users.get(0).getLocation().getTime()) <
// 1000);
// assertEquals("Second location's latitude does not match",
// location2.getLatitude(), users.get(1)
// .getLocation().getLatitude());
// assertEquals("Second location's longitude does not match",
// location2.getLongitude(), users.get(1)
// .getLocation().getLongitude());
// assertTrue("Last seen of second user does not match",
// Math.abs(date2.getTimeInMillis() - users.get(1).getLocation().getTime()) <
// 1000);
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
//
// }
//
// @Test
// public void testParsePositionsWhenEmptyList() throws SmartMapParseException {
// SmartMapParser parser = new JsonSmartMapParser();
<<<<<<< HEAD
// List<ImmutableUser> users = parser.parsePositions(PROPER_POSITIONS_EMPTY_LIST_JSON);
=======
// List<ImmutableUser> users =
// parser.parsePositions(PROPER_POSITIONS_EMPTY_LIST_JSON);
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// assertTrue("Did not parsed empty positions list correctly", users.isEmpty());
// }
//
// private void checkEvent1(ImmutableEvent immutableEvent) {
//
// assertEquals("creator id does not match", 3, immutableEvent.getCreatorId());
// assertEquals("event id does not match", 13, immutableEvent.getID());
// assertTrue("End date does not match",
<<<<<<< HEAD
// Math.abs(date1.getTimeInMillis() - immutableEvent.getEndDate().getTimeInMillis()) < 1000);
// assertTrue("Start date does not match",
// Math.abs(date2.getTimeInMillis() - immutableEvent.getStartDate().getTimeInMillis()) < 1000);
=======
// Math.abs(date1.getTimeInMillis() -
// immutableEvent.getEndDate().getTimeInMillis()) < 1000);
// assertTrue("Start date does not match",
// Math.abs(date2.getTimeInMillis() -
// immutableEvent.getStartDate().getTimeInMillis()) < 1000);
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// assertEquals("latitude does not match", location1.getLatitude(),
// immutableEvent.getLocation().getLatitude());
// assertEquals("Friend's longitude does not match", location1.getLongitude(),
// immutableEvent.getLocation().getLongitude());
<<<<<<< HEAD
// assertEquals("Position name does not match", "Tokyo", immutableEvent.getLocationString());
// assertEquals("Event name does not match", "MyEvent", immutableEvent.getName());
// assertEquals("Event description does not match", "description", immutableEvent.getDescription());
//
// assertTrue("Did not parse the 3 participants", immutableEvent.getParticipants().size() == 3);
// assertEquals("First participant do not match", Long.valueOf(3), immutableEvent.getParticipants().get(0));
// assertEquals("Second participant do not match", Long.valueOf(4), immutableEvent.getParticipants().get(1));
// assertEquals("Third participant do not match", Long.valueOf(1), immutableEvent.getParticipants().get(2));
=======
// assertEquals("Position name does not match", "Tokyo",
// immutableEvent.getLocationString());
// assertEquals("Event name does not match", "MyEvent",
// immutableEvent.getName());
// assertEquals("Event description does not match", "description",
// immutableEvent.getDescription());
//
// assertTrue("Did not parse the 3 participants",
// immutableEvent.getParticipants().size() == 3);
// assertEquals("First participant do not match", Long.valueOf(3),
// immutableEvent.getParticipants().get(0));
// assertEquals("Second participant do not match", Long.valueOf(4),
// immutableEvent.getParticipants().get(1));
// assertEquals("Third participant do not match", Long.valueOf(1),
// immutableEvent.getParticipants().get(2));
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
//
// }
//
// private void checkEvent2(ImmutableEvent event) {
// assertEquals("creator id does not match", 1, event.getCreatorId());
// assertEquals("event id does not match", 11, event.getID());
// assertTrue("End date does not match",
<<<<<<< HEAD
// Math.abs(date4.getTimeInMillis() - event.getEndDate().getTimeInMillis()) < 1000);
// assertTrue("Start date does not match",
// Math.abs(date3.getTimeInMillis() - event.getStartDate().getTimeInMillis()) < 1000);
// assertEquals("latitude does not match", location2.getLatitude(), event.getLocation().getLatitude());
// assertEquals("Friend's longitude does not match", location2.getLongitude(),
// event.getLocation().getLongitude());
// assertEquals("Position name does not match", "London", event.getLocationString());
// assertEquals("Event name does not match", "YourEvent", event.getName());
// assertEquals("Event description does not match", "description2", event.getDescription());
// assertTrue("Did not parse the participant", event.getParticipants().size() == 1);
// assertEquals("First participant do not match", Long.valueOf(1), event.getParticipants().get(0));
=======
// Math.abs(date4.getTimeInMillis() - event.getEndDate().getTimeInMillis()) <
// 1000);
// assertTrue("Start date does not match",
// Math.abs(date3.getTimeInMillis() - event.getStartDate().getTimeInMillis()) <
// 1000);
// assertEquals("latitude does not match", location2.getLatitude(),
// event.getLocation().getLatitude());
// assertEquals("Friend's longitude does not match", location2.getLongitude(),
// event.getLocation().getLongitude());
// assertEquals("Position name does not match", "London",
// event.getLocationString());
// assertEquals("Event name does not match", "YourEvent", event.getName());
// assertEquals("Event description does not match", "description2",
// event.getDescription());
// assertTrue("Did not parse the participant", event.getParticipants().size() ==
// 1);
// assertEquals("First participant do not match", Long.valueOf(1),
// event.getParticipants().get(0));
>>>>>>> 39092ebfcd7ec3e217b3d3b2da359e53a13b9813
// }
// }