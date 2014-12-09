package ch.epfl.smartmap.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.epfl.smartmap.R;
import ch.epfl.smartmap.background.ServiceContainer;
import ch.epfl.smartmap.map.DefaultZoomManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This activity lets the user select a new position on the map
 * 
 * @author agpmilli
 */
public class SetLocationActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SetLocationActivity.class.getSimpleName();

    private static final int GOOGLE_PLAY_REQUEST_CODE = 10;
    private static final String CITY_NAME = "CITY_NAME";
    static final int PICK_LOCATION_REQUEST = 1;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mFragmentMap;
    private LatLng mMyPosition;
    private LatLng mEventPosition;

    /**
     * Display the map with the current location
     */
    public void displayMap() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, GOOGLE_PLAY_REQUEST_CODE);
            dialog.show();
        } else {
            // Google Play Services are available.
            // Getting reference to the SupportMapFragment of activity_main.xml
            mFragmentMap =
                (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.set_location_map);
            // Getting GoogleMap object from the fragment
            mGoogleMap = mFragmentMap.getMap();
            mGoogleMap.setMyLocationEnabled(true);

            // Get my position from SettingsManager
            mMyPosition =
                new LatLng(ServiceContainer.getSettingsManager().getLocation().getLatitude(),
                    ServiceContainer.getSettingsManager().getLocation().getLongitude());

            mEventPosition = this.getIntent().getParcelableExtra(AddEventActivity.LOCATION_EXTRA);

            // Enabling MyLocation Layer of Google Map
            new DefaultZoomManager(mFragmentMap).zoomWithAnimation(mEventPosition);

            mGoogleMap.addMarker(new MarkerOptions().position(mEventPosition).draggable(true));

            mGoogleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

                @Override
                public void onMarkerDrag(Marker marker) {
                    mEventPosition = marker.getPosition();
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    mEventPosition = marker.getPosition();
                }

                @Override
                public void onMarkerDragStart(Marker marker) {
                    mEventPosition = marker.getPosition();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_set_location);

        // Makes the logo clickable (clicking it returns to previous activity)
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.color.main_blue));

        // Maybe set it longer, how?
        Toast.makeText(this, this.getString(R.string.set_location_toast), Toast.LENGTH_LONG).show();

        this.displayMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.set_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {
            case R.id.set_location_done:
                Intent addEventIntent = new Intent(this, AddEventActivity.class);
                addEventIntent.putExtra(AddEventActivity.LOCATION_EXTRA, mEventPosition);
                this.setResult(RESULT_OK, addEventIntent);
                this.finish();
            case android.R.id.home:
                this.finish();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
