package edu.virginia.sde.hw5;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StopReader {

    private final URL busStopsApiUrl;

    public StopReader(Configuration configuration) {
        this.busStopsApiUrl = configuration.getBusStopsURL();
    }

    /**
     * Read all the stops from the "stops" json URL from Configuration Reader
     * @return List of stops
     */
    public List<Stop> getStops() {
        List returnStops = new ArrayList<>();
        //TODO: implement
        //COMPLETE
        WebServiceReader webServiceReader = new WebServiceReader(busStopsApiUrl);
        JSONObject jsonObject = webServiceReader.getJSONObject();
        JSONArray stops = jsonObject.getJSONArray("stops");

        for(int i = 0; i < stops.length(); i++){
            int id = stops.getJSONObject(i).getInt("id");
            String name = stops.getJSONObject(i).getString("name");
            double latitude = stops.getJSONObject(i).getJSONArray("position").getDouble(0);
            double longtitude = stops.getJSONObject(i).getJSONArray("position").getDouble(1);
            Stop stop = new Stop(id, name, latitude, longtitude);
            returnStops.add(stop);
        }
        return returnStops;
    }
}
