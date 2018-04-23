package cs.dal.a4176_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tedzhao on 2017/11/19.
 */

class DataParser {

    // pass direction json array to get duration hash map
    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance ="";

        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return googleDirectionsMap;
    }

    // pass google json to get place hash map
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        Log.d("getPlace", "Entered");


        try {
            if(!googlePlaceJson.isNull("name"))
            {
                    placeName = googlePlaceJson.getString("name");
            }
            if( !googlePlaceJson.isNull("vicinity"))
            {
                vicinity = googlePlaceJson.getString("vicinity");

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name" , placeName);
            googlePlacesMap.put("vicinity" , vicinity);
            googlePlacesMap.put("lat" , latitude);
            googlePlacesMap.put("lng" , longitude);
            googlePlacesMap.put("reference" , reference);


            Log.d("getPlace", "Putting Places");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }


    // pass json array to get places hash map
    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;
        Log.d("Places", "getPlaces");

        for(int i = 0;i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;

    }

    // parse json to store in a list
    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");

            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    // pass json directions to store in string array
    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    // parse json data to get positions store in hasp map
    public HashMap<String,String> parsePositions(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs"); //routes array
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getDuration(jsonArray);

    }

    // pass json to get paths in string array
    public String[] getPaths(JSONArray googleStepsJson )
    {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    // pass json to get path string array
    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }

    // pass json to store record path in hash map
    private HashMap<String,String> getRecordPath(JSONObject googlePathJson)
    {
        HashMap<String, String> googlePathMap = new HashMap<>();

        try {
            String start_lat = googlePathJson.getJSONObject("start_location").getString("lat");
            String start_lng = googlePathJson.getJSONObject("start_location").getString("lng");
            String end_lat = googlePathJson.getJSONObject("end_location").getString("lat");
            String end_lng = googlePathJson.getJSONObject("end_location").getString("lng");
            String polyline = googlePathJson.getJSONObject("polyline").getString("points");

            googlePathMap.put("start_lat" , start_lat);
            googlePathMap.put("start_lng", start_lng);
            googlePathMap.put("end_lat" , end_lat);
            googlePathMap.put("end_lng", end_lng);
            googlePathMap.put("polyline", polyline);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePathMap;
    }

    // pass json directions to get record paths stored in a list of hash map
    private List<HashMap<String,String>> getRecordPaths(JSONArray googleDirectionsJson)
    {
        List<HashMap<String,String>> googleDirectionsList = new ArrayList<>();
        JSONObject googlePathJson = null;
        int count_paths = googleDirectionsJson.length();


        for(int i = 0;i<count_paths;i++)
        {
            try {
                googlePathJson = googleDirectionsJson.getJSONObject(i);
                googleDirectionsList.add(getRecordPath(googlePathJson));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return googleDirectionsList;
    }


}
