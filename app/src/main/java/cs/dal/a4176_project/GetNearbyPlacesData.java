package cs.dal.a4176_project;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tedzhao on 2017/11/19.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        // get download url object
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        // create new data parser
        DataParser parser = new DataParser();
        // parse data into list
        nearbyPlaceList = parser.parse(s);
        // show places on map according to the list
        showNearbyPlaces(nearbyPlaceList);
    }

    // show nearby places according to a hash map list
    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList)
    {
        for(int i = 0;i<nearbyPlaceList.size() ; i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String , String> googlePlace = nearbyPlaceList.get(i);
            Log.d("onPostExecute","Entered into showing locations");
            // get place name
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            // parse double
            double lat = Double.parseDouble( googlePlace.get("lat") );
            double lng = Double.parseDouble( googlePlace.get("lng"));
            // create new lat lng
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName +" : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        }
    }
}
