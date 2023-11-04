package edu.virginia.sde.hw5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BusLineReader {
    private final URL busLinesApiUrl;
    private final URL busStopsApiUrl;

    /* You'll need this to get the Stop objects when building the Routes object */
    private final StopReader stopReader;
    List<Stop> stops = null;
    /**
     * Returns a list of BusLine objects. This is a "deep" list, meaning all the BusLine objects
     * already have their Route objects fully populated with that line's Stops.
     */

    public BusLineReader(Configuration configuration) {
        this.busStopsApiUrl = configuration.getBusStopsURL();
        this.busLinesApiUrl = configuration.getBusLinesURL();
        stopReader = new StopReader(configuration);
        stops = stopReader.getStops(); //list of all bus stops
    }

    /**
     * This method returns the BusLines from the API service, including their
     * complete Routes.
     */
    public List<BusLine> getBusLines() {
        List returnLines = new ArrayList<>();

        //TODO: implement
        //COMPLETE
        WebServiceReader webServiceReader = new WebServiceReader(busStopsApiUrl);
        JSONObject jsonStopObject = webServiceReader.getJSONObject();
        webServiceReader = new WebServiceReader(busLinesApiUrl);
        JSONObject jsonLineObject = webServiceReader.getJSONObject();
        JSONArray lines = jsonLineObject.getJSONArray("lines");

        for(int i = 0; i < lines.length(); i++){
            //loop through all the lines
            int id = lines.getJSONObject(i).getInt("id");
            boolean is_active = lines.getJSONObject(i).getBoolean("is_active");
            String long_name = lines.getJSONObject(i).getString("long_name");
            String short_name = lines.getJSONObject(i).getString("short_name");


            List<Stop> busLineStop = new ArrayList<>();
            for(int j = 0; j < jsonStopObject.getJSONArray("routes").length();j++){
                //loop through all the route
                if(jsonStopObject.getJSONArray("routes").getJSONObject(j).getInt("id")==id){
                    //find the corresponding id and its stops list
                    for (int k =0; k < jsonStopObject.getJSONArray("routes").getJSONObject(j).getJSONArray("stops").length();k++){
                        busLineStop.add(findStop(jsonStopObject.getJSONArray("routes").getJSONObject(j).getJSONArray("stops").getInt(k)));
                    }
                }
            }

            Route route = new Route(busLineStop);
            BusLine busline = new BusLine(id, is_active, long_name, short_name,route);
            returnLines.add(busline);
        }
        return returnLines;
    }

    public Stop findStop(int id){
        for(int i = 0; i < stops.size();i++){
            if (stops.get(i).getId() == id){
                return stops.get(i);
            }
        }
        return null;
    }
}
