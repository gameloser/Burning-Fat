package cs.dal.a4176_project;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by tedzhao on 2017/11/19.
 */

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    static GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    private static String parsedDir = "";
    static List<Polyline> polylinesList = null;

    GetDirectionsData () {
        if (polylinesList == null)
            polylinesList = new ArrayList<>();
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        String[] directionsList;
        HashMap<String,String> positionsList;
        // create new parser
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        positionsList = parser.parsePositions(s);
        // get duration and distance
        duration = positionsList.get("duration");
        distance = positionsList.get("distance");
        // clear map and add new markers
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions);
        // set text view
        MapsActivity.tv_distance.setText(distance);
        MapsActivity.tv_duration.setText(duration);
        setAddress(MapsActivity.context);
        // display directions
        displayDirection(directionsList);
    }

    // display the direction according to the points the string array
    public void displayDirection(String[] directionsList)
    {
        for(String s : directionsList)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.parseColor("#1a8cff"));
            options.width(22);
            options.addAll(PolyUtil.decode(s));

            polylinesList.add(mMap.addPolyline(options));
        }
    }

    // remove polyline drew
    public static void removeDirection() {
        if (polylinesList != null)
            for (Polyline p : polylinesList)
                p.remove();
    }

    // get address from lat lng
    public void setAddress(Context context) {

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                MapsActivity.searchBar.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

