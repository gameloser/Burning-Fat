package cs.dal.a4176_project;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    double end_latitude, end_longitude;
    static Context context;
    static EditText searchBar;
    static TextView tv_distance, tv_duration;
    private final float THRESHOLD = 100 / 1000f;
    private boolean MOVE_STATE = false;
    ImageButton start_stop;
    static int DIST_STATE = 0;
    private double temp_start_lat, temp_start_lng;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchBar = findViewById(R.id.searchbar);
        tv_distance = findViewById(R.id.tv_distance);
        tv_duration = findViewById(R.id.tv_duration);
        start_stop = findViewById(R.id.btn_start_stop);
        start_stop.setVisibility(View.GONE);
        databaseManager = new DatabaseManager(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    // sync method build api client connection
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch(v.getId()) {
            case R.id.btn_search: {
                EditText tf_location = (EditText) findViewById(R.id.searchbar);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();
                Log.d("location = ", location);

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            markerOptions.position(latLng);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }

                }
            }
            break;
            case R.id.btn_hospital:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_start_stop:
                // change state
                if (!MOVE_STATE) {
                    MOVE_STATE = true;
                    Toast.makeText(MapsActivity.this, "Start training!", Toast.LENGTH_LONG).show();
                    // change image icon
                    start_stop.setBackground(getDrawable(R.drawable.stop_map));
                }
                else {
                    MOVE_STATE = false;
                    Toast.makeText(MapsActivity.this, "Stop training!", Toast.LENGTH_LONG).show();
                    // change image icon
                    start_stop.setBackground(getDrawable(R.drawable.start_map));
                    handleResult(false);
                }
                break;

        }
    }

    private void handleResult(boolean finished) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (finished) {
            builder.setTitle("Good Job!");
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MapsActivity.this);
                    builder2.setTitle("Save record");
                    builder2.setMessage("Enter a title");

                    final EditText input = new EditText(builder.getContext());
                    input.setWidth(100);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    builder2.setView(input);


                    builder2.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // parse distance -- to km
                            String tv_distanceText = (String) tv_distance.getText();
                            String partsd[] = tv_distanceText.split(" ");

                            float dist = Float.parseFloat(partsd[0]);
                            String unit1 = partsd[1];

                            if (unit1.equals("m"))
                                dist = dist / 1000;

                            // parse time
                            String tv_durationText = (String) tv_duration.getText();
                            String partst[] = tv_durationText.split(" ");

                            final String time = partst[0];
                            String unit2 = partst[1];

                            String type = "";

                            if (TypeSelectActivity.TYPE == 0)
                                type = "Run";
                            else if (TypeSelectActivity.TYPE == 1)
                                type = "Bicycle";

                            Calendar c = Calendar.getInstance();
                            String currentDate = new SimpleDateFormat("yyyy-M-d").format(c.getTime());


                            String title = input.getText().toString();
                            Toast.makeText(MapsActivity.this, "Record Saved!", Toast.LENGTH_LONG).show();
                            databaseManager.addSportRecords(type, title, currentDate, time, dist, dist, 1f, 120f, 66f);


                            finish();
                            startActivity(getIntent());
                        }
                    });

                    builder2.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });

                    AlertDialog alert2 = builder2.create();
                    alert2.show();

                }
            });

            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });

            String result = "Distance: " + tv_distance.getText() + "\n";
            result += "Time: " + tv_duration.getText() + "\n";
            ImageView imageView = new ImageView(builder.getContext());
            imageView.setImageResource(R.drawable.finish_2);
            builder.setView(imageView);
            builder.setMessage(result);
        }
        else {
            builder.setTitle("Not Finished!");
            builder.setPositiveButton("New Task", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });

            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });

            ImageView imageView = new ImageView(builder.getContext());
            imageView.setImageResource(R.drawable.crying);
            builder.setView(imageView);
        }

        if (TypeSelectActivity.TYPE == 0)
            builder.setIcon(R.drawable.running);
        else if (TypeSelectActivity.TYPE == 1)
            builder.setIcon(R.drawable.bicycle);
        AlertDialog alert1 = builder.create();
        alert1.show();

    }

    // get direction url
    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
    }

    // get url
    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16f));

        setAddress(getApplicationContext(), latitude, longitude);

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        GetDirectionsData.removeDirection();
        context = getApplicationContext();
    }

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // reset lat lng
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        // get direction data array
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(end_latitude, end_longitude);
        getDirectionsData.execute(dataTransfer);

        // set image button to visible
        start_stop.setVisibility(View.VISIBLE);
        // enough distance to start
        Toast.makeText(this, "Ready on start!", Toast.LENGTH_LONG).show();

    }

    public void setAddress(Context context, double latitude, double longitude) {

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0);
                //searchBar.setText(address);
                Toast.makeText(context,"Your Current Location\n" + address, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

