package ch.epfl.smartmap.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.TimeZone;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import ch.epfl.smartmap.R;
import ch.epfl.smartmap.background.ServiceContainer;
import ch.epfl.smartmap.background.SettingsManager;
import ch.epfl.smartmap.cache.EventContainer;
import ch.epfl.smartmap.cache.PublicEvent;
import ch.epfl.smartmap.callbacks.NetworkRequestCallback;
import ch.epfl.smartmap.gui.DatePickerFragment;
import ch.epfl.smartmap.gui.TimePickerFragment;
import ch.epfl.smartmap.map.DefaultZoomManager;
import ch.epfl.smartmap.util.Utils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This activity lets the user create a new event.
 * 
 * @author SpicyCH
 * @author agpmilli
 */
public class AddEventActivity extends FragmentActivity {

    /**
     * Used as a key to pass LatLng through Intents
     */
    public static final String LOCATION_EXTRA = "LOCATION";

    private static final String TAG = AddEventActivity.class.getSimpleName();

    private static final int GOOGLE_PLAY_REQUEST_CODE = 10;
    static final int PICK_LOCATION_REQUEST = 1;

    private static final int ELEMENTS_HH_MM = 2;
    private static final int ELEMENTS_JJ_DD_YYYY = 3;
    private static final int INDEX_YEAR = 2;

    private static final int INDEX_MONTH = 1;
    private static final int INDEX_DAY = 0;

    private static final String TIME_PICKER_DESCR = "timePicker";
    private static final String DATE_PICKER_DESCR = "datePicker";
    private static final int MAX_NAME_SIZE = 60;

    private static final int MIN_NAME_SIZE = 2;

    private static final int MAX_DESCRIPTION_SIZE = 255;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mFragmentMap;
    private LatLng mEventPosition;
    private EditText mDescription;
    private EditText mEventName;
    private EditText mPickEndDate;
    private EditText mPickEndTime;
    private EditText mPickStartDate;

    private EditText mPickStartTime;

    private EditText mPlaceName;

    private TextWatcher mTextChangedListener;

    private Calendar mStartDate;
    private Calendar mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_event);

        // Makes the logo clickable (clicking it returns to previous activity)
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.color.main_blue));

        this.initializeGUIComponents();

        mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                if (mEventPosition != null) {
                    mGoogleMap.clear();
                }
                Intent setLocationIntent = new Intent(AddEventActivity.this, SetLocationActivity.class);
                setLocationIntent.putExtra(LOCATION_EXTRA, mEventPosition);
                AddEventActivity.this.startActivityForResult(setLocationIntent, PICK_LOCATION_REQUEST);
            }
        });

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            LatLng latLng = extras.getParcelable(LOCATION_EXTRA);
            if ((latLng != null) && (Math.abs(latLng.latitude) > 0)) {
                // The user long clicked the map in MainActivity and wants to
                // create an event
                this.updateLocation(this.getIntent());
            }
        }
    }

    /**
     * Display the map with the current location
     */
    public void displayMap() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // Showing status
        if (status != ConnectionResult.SUCCESS) {
            // Google Play Services are not available
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, GOOGLE_PLAY_REQUEST_CODE);
            dialog.show();
        } else {
            // Google Play Services are available.
            // Getting reference to the SupportMapFragment of activity_main.xml
            mFragmentMap = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.add_event_map);
            // Getting GoogleMap object from the fragment
            mGoogleMap = mFragmentMap.getMap();
            // Enabling MyLocation Layer of Google Map
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            mGoogleMap.clear();

            mGoogleMap.addMarker(new MarkerOptions().position(mEventPosition));

            mPlaceName.setText(ServiceContainer.getSettingsManager().getLocationName());

            new DefaultZoomManager(mFragmentMap).zoomWithAnimation(new LatLng(ServiceContainer.getSettingsManager()
                    .getLocation().getLatitude(), ServiceContainer.getSettingsManager().getLocation().getLongitude()));

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                // All went smoothly, update location in this activity
                this.updateLocation(data);

            } else {
                // Google wasn't able to retrieve the location name associated
                // to the coordinates
                Toast.makeText(AddEventActivity.this,
                        this.getString(R.string.add_event_toast_couldnt_retrieve_location), Toast.LENGTH_LONG).show();
                mPlaceName.setText("");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.addEventButtonCreateEvent:
                this.createEvent();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @return <code>true</code> if all the fields (event name, event dates, etc...) are legally set and the event is
     *         ready to be created.
     * @author SpicyCH
     */
    private boolean allFieldsSetByUser() {
        boolean validEndDateTime = this.isValidTime(mPickEndTime.getText().toString());

        boolean validPosition = (mEventPosition.latitude != 0) && (mEventPosition.longitude != 0);

        return validEndDateTime && validPosition
                && (!"".equals(mPlaceName.getText().toString()) && !"".equals(mEventName.getText().toString()));
    }

    /**
     * Ensures the end of the event is after its start and end of the event is not in the past. Displays a toast and
     * reset the bad field set by the user if necessary.
     * 
     * @param startDate
     * @param startTime
     * @param endDate
     * @param endTime
     * @author SpicyCH
     */
    private void checkDatesValidity(EditText startDate, EditText startTime, EditText endDate, EditText endTime) {

        Log.d(TAG, "Checking dates validity.\nmStartDate:\n" + mStartDate + "mEndDate:\n" + mEndDate);

        if (this.isValidDate(endDate.getText().toString()) && this.isValidTime(endTime.getText().toString())) {
            // The end of the event has been set by the user

            Calendar now = GregorianCalendar.getInstance(TimeZone.getTimeZone(Utils.GMT_SWITZERLAND));

            // Needed to let the user click the default time without errors.
            now.add(GregorianCalendar.MINUTE, -1);

            if (mEndDate.before(mStartDate)) {
                // The user is trying to create the end of the event before its
                // start!

                this.resetFields(endDate, endTime);

                Toast.makeText(AddEventActivity.this,
                        this.getString(R.string.add_event_toast_event_cannot_end_before_starting), Toast.LENGTH_LONG)
                        .show();
            } else if (mEndDate.before(now)) {
                // The user is trying to create an event in the past

                this.resetFields(endDate, endTime);

                Toast.makeText(AddEventActivity.this,
                        this.getString(R.string.add_event_toast_event_end_cannot_be_in_past), Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Creates an event from the user specified parameters.
     * 
     * @author SpicyCH
     */
    private void createEvent() {

        if (!this.allFieldsSetByUser()) {

            Toast.makeText(AddEventActivity.this, this.getString(R.string.add_event_toast_not_all_fields_set),
                    Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Couldn't create a new event because not all fields were set.\n" + "end date: "
                    + mPickEndDate.getText().toString() + "\n" + "end time: " + mPickEndTime.getText().toString()
                    + "\n" + "event name: " + mEventName.getText().toString() + "\n" + "event place name: "
                    + mPlaceName.getText().toString() + "\n" + "event lat/long: " + mEventPosition.latitude + "/"
                    + mEventPosition.longitude);

        } else if (!this.fieldsHaveLegalLength()) {
            Toast.makeText(AddEventActivity.this, this.getString(R.string.add_event_toast_fields_bad_size),
                    Toast.LENGTH_LONG).show();
        } else {

            Location location = new Location("Location set by user");
            location.setLatitude(mEventPosition.latitude);
            location.setLongitude(mEventPosition.longitude);

            EventContainer event = new EventContainer(PublicEvent.NO_ID, mEventName.getText().toString(),
                    ServiceContainer.getCache().getSelf().getImmutableCopy(), mDescription.getText().toString(),
                    mStartDate, mEndDate, location, mPlaceName.getText().toString(), new HashSet<Long>());

            ServiceContainer.getCache().createEvent(event, new CreateEventNetworkCallback());
        }
    }

    /**
     * @return <code>true</code> if all fields' length are legal, <code>false</code> otherwise.
     * @author SpicyCH
     */
    private boolean fieldsHaveLegalLength() {

        int eventNameSize = mEventName.getText().toString().length();
        boolean eventNameLegal = !((eventNameSize < MIN_NAME_SIZE) || (eventNameSize > MAX_NAME_SIZE));

        int placeNameSize = mPlaceName.getText().toString().length();
        boolean placeNameLegal = !((placeNameSize < MIN_NAME_SIZE) || (placeNameSize > MAX_NAME_SIZE));

        int descrSize = mDescription.getText().toString().length();
        boolean descrLegal = descrSize < MAX_DESCRIPTION_SIZE;

        return eventNameLegal && placeNameLegal && descrLegal;
    }

    /**
     * @author SpicyCH
     */
    private void initializeGUIComponents() {
        mEventName = (EditText) this.findViewById(R.id.addEventEventName);
        mPickStartDate = (EditText) this.findViewById(R.id.addEventEventDate);
        mPickStartTime = (EditText) this.findViewById(R.id.addEventEventTime);
        mPickEndTime = (EditText) this.findViewById(R.id.addEventEndTime);
        mPickEndDate = (EditText) this.findViewById(R.id.addEventEndDate);
        mDescription = (EditText) this.findViewById(R.id.addEventDescription);
        mPlaceName = (EditText) this.findViewById(R.id.addEventPlaceName);

        mPlaceName.setFocusable(true);

        // Workaround to let espresso tests pass
        if (ServiceContainer.getSettingsManager() == null) {
            ServiceContainer.setSettingsManager(new SettingsManager(this));
        }

        // Initialize mEventPosition to position of user
        mEventPosition = new LatLng(ServiceContainer.getSettingsManager().getLocation().getLatitude(), ServiceContainer
                .getSettingsManager().getLocation().getLongitude());

        mTextChangedListener = new DateChangedListener();

        mPickStartDate.addTextChangedListener(mTextChangedListener);
        mPickStartTime.addTextChangedListener(mTextChangedListener);
        mPickEndDate.addTextChangedListener(mTextChangedListener);
        mPickEndTime.addTextChangedListener(mTextChangedListener);

        mStartDate = GregorianCalendar.getInstance(TimeZone.getTimeZone(Utils.GMT_SWITZERLAND));
        mEndDate = GregorianCalendar.getInstance(TimeZone.getTimeZone(Utils.GMT_SWITZERLAND));

        mPickStartTime.setText(Utils.getTimeString(mStartDate));

        mPickStartTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment(mPickStartTime, mStartDate);
                newFragment.show(AddEventActivity.this.getSupportFragmentManager(), TIME_PICKER_DESCR);
            }

        });

        mPickStartDate.setText(Utils.getDateString(mStartDate));

        mPickStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(mPickStartDate, mStartDate);
                newFragment.show(AddEventActivity.this.getSupportFragmentManager(), DATE_PICKER_DESCR);
            }
        });

        mPickEndDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(mPickEndDate, mEndDate);
                newFragment.show(AddEventActivity.this.getSupportFragmentManager(), DATE_PICKER_DESCR);
            }
        });

        mPickEndTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment(mPickEndTime, mEndDate);
                newFragment.show(AddEventActivity.this.getSupportFragmentManager(), TIME_PICKER_DESCR);
            }
        });
        this.displayMap();
    }

    private boolean isValidDate(String s) {
        String[] sArray = s.split(".");
        return sArray.length == ELEMENTS_JJ_DD_YYYY;
    }

    private boolean isValidTime(String s) {
        String[] sArray = s.split(":");
        return sArray.length == ELEMENTS_HH_MM;
    }

    /**
     * Reset the two given EditTexts.
     * 
     * @param first
     * @param second
     * @author SpicyCH
     */
    private void resetFields(EditText first, EditText second) {
        first.setText("");
        second.setText("");
    }

    /**
     * @param data
     *            the intent containing the extras. The position (LatLgn) is retrieved from the
     *            getParcelable(LOCATION_SERVICE).
     * @author SpicyCH
     */
    private void updateLocation(Intent data) {
        Bundle extras = data.getExtras();
        mEventPosition = extras.getParcelable(LOCATION_EXTRA);

        if (mEventPosition != null) {
            Location location = new Location("");
            location.setLatitude(mEventPosition.latitude);
            location.setLongitude(mEventPosition.longitude);

            String cityName = Utils.getCityFromLocation(location);

            if ((cityName != null) && !cityName.isEmpty()) {
                mPlaceName.setText(cityName);
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(mEventPosition));
                new DefaultZoomManager(mFragmentMap).zoomWithAnimation(mEventPosition);
            } else {
                Toast.makeText(AddEventActivity.this,
                        this.getString(R.string.add_event_toast_couldnt_retrieve_location_name), Toast.LENGTH_LONG)
                        .show();
                mPlaceName.setText("");
            }
        } else {
            Log.e(TAG, "No event position set (extras.getParcelable(LOCATION_EXTRA) was null)");
            Toast.makeText(AddEventActivity.this, this.getString(R.string.error_client_side), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Callback
     * 
     * @author SpicyCH
     */
    private class CreateEventNetworkCallback implements NetworkRequestCallback {
        @Override
        public void onFailure() {
            AddEventActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddEventActivity.this,
                            AddEventActivity.this.getString(R.string.add_event_toast_couldnt_create_event_server),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess() {
            AddEventActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddEventActivity.this,
                            AddEventActivity.this.getString(R.string.add_event_toast_event_created), Toast.LENGTH_SHORT)
                            .show();

                    AddEventActivity.this.finish();
                }
            });
        }
    }

    /**
     * Listener on the TextViews used to display the dates
     * 
     * @author SpicyCH
     */
    private class DateChangedListener implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            // Remove the TextChangedListener to avoid useless calls
            // triggered by the following code
            mPickEndDate.removeTextChangedListener(mTextChangedListener);
            mPickStartDate.removeTextChangedListener(mTextChangedListener);

            AddEventActivity.this.checkDatesValidity(mPickStartDate, mPickStartTime, mPickEndDate, mPickEndTime);

            // Reset the TextChangedListener
            mPickEndDate.addTextChangedListener(mTextChangedListener);
            mPickStartDate.addTextChangedListener(mTextChangedListener);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Nothing
        }
    }
}