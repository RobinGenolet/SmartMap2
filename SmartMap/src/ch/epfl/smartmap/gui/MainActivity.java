package ch.epfl.smartmap.gui;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import ch.epfl.smartmap.R;
import ch.epfl.smartmap.cache.MockDB;
import ch.epfl.smartmap.cache.Friend;
import ch.epfl.smartmap.cache.MockPictureProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * @author jfperren
 * 
 */
public class MainActivity extends FragmentActivity implements LocationListener {

	private static final String TAG = "GoogleMap";
	private static final int LOCATION_UPDATE_TIMEOUT = 20000;
	private static final int GOOGLE_PLAY_REQUEST_CODE = 10;
	private static final int GMAP_ZOOM_LEVEL = 15;

	@SuppressWarnings("unused")
	private DrawerLayout mSideDrawerLayout;
	private String[] mSideLayoutElements;
	private ListView mDrawerListView;

	MockDB mockDB = new MockDB();
	List<Friend> mListOfFriends;
	private GoogleMap mGoogleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initializeDrawerLayout();
		if (savedInstanceState == null) {
			displayMap();
		}
		if (mGoogleMap != null) {
			mListOfFriends = mockDB.getMockFriends();
			// = listFriendPos();
			FriendMarker friendMarker = new FriendMarker();
			friendMarker.setMarkersToMaps(this, mGoogleMap, mListOfFriends);
			zoomAccordingToMarkers();

		}

		/*
		 * BELOW GOES SEARCHBAR & SLIDINGUPPANEL INIT
		 */
		final EditText mSearchBarEditText = (EditText) findViewById(R.id.searchBarEditText);
		final SlidingUpPanelLayout mBottomSlider = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mBottomSlider.setCoveredFadeColor(0);
		mBottomSlider.collapsePanel();

		mSearchBarEditText
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						boolean handled = false;
						if (actionId == EditorInfo.IME_ACTION_SEND) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							Log.d("searchBar", "Search Request Finished");
							handled = true;
						}
						return handled;
					}
				});

		mSearchBarEditText
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							mBottomSlider.expandPanel();
							Log.d("SearchBar", "Got focus");
						} else {
							mBottomSlider.hidePanel();
							Log.d("SearchBar", "Lost focus");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		zoomMap(location);
		//updatePos
	}

	@Override
	public void onBackPressed() {
		final SlidingUpPanelLayout mBottomSlider = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

		if (mBottomSlider != null && mBottomSlider.isPanelExpanded()
				|| mBottomSlider.isPanelAnchored()) {
			mBottomSlider.collapsePanel();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Called to set up the left side menu. Supposedly called only once.
	 */
	private void initializeDrawerLayout() {
		// Get ressources
		mSideLayoutElements = getResources().getStringArray(
				R.array.sideMenuElements);
		mSideDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_drawer);
		// Set the adapter for the listView
		mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mSideLayoutElements));
		mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
	}

	public Context getContext() {
		return this;
	}

	// this part concerns the set bound and zoom with regards to all marker
	// positions on the map
	void zoomAccordingToMarkers() {
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							LatLng centre = new LatLng(47.20380010, 2.00168440);
							LatLngBounds bounds = new LatLngBounds.Builder()
									.include(centre).build();
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mGoogleMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
							mGoogleMap.animateCamera(CameraUpdateFactory
									.zoomTo(5));

						}
					});
		}

	}

	/**
	 * Zoom on the location "location"
	 * 
	 * @param location
	 */
	public void zoomMap(Location location) {
		Log.d(TAG, "zoomMap called");
		LatLng latLng1 = new LatLng(location.getLatitude(),
				location.getLongitude());
		Log.d(TAG, "1");
		// Zoom in the Google Map
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
		Log.d(TAG, "1");
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(GMAP_ZOOM_LEVEL));
		Log.d(TAG, "1");
	}

	/**
	 * Display the map with the current location
	 */
	public void displayMap() {
		Log.d(TAG, "displayMap called");
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());
		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available
			Log.d(TAG, "no Google Play Services");

			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					GOOGLE_PLAY_REQUEST_CODE);
			dialog.show();

		} else { // Google Play Services are available

			Log.d(TAG, "Google Play Services OK");

			// Getting reference to the SupportMapFragment of activity_main.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			// Getting GoogleMap object from the fragment
			mGoogleMap = fm.getMap();
			// Enabling MyLocation Layer of Google Map
			mGoogleMap.setMyLocationEnabled(true);
			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();
			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);
			Log.d(TAG, "provider : " + provider);
			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (isGPSEnabled) {
				Log.d(TAG, "gps enabled");
			}

			if (location != null) {
				onLocationChanged(location);
				zoomMap(location);
				locationManager.requestLocationUpdates(provider,
						LOCATION_UPDATE_TIMEOUT, 0, this);
			} else {
				Log.d(TAG, "Error retrieving location from GoogleMap Servers.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
