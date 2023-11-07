package edu.virginia.sde.hw5;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BusLineService {
    private final DatabaseDriver databaseDriver;

    public BusLineService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void addStops(List<Stop> stops) {
        try {
            databaseDriver.connect();
            databaseDriver.addStops(stops);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBusLines(List<BusLine> busLines) {
        try {
            databaseDriver.connect();
            databaseDriver.addBusLines(busLines);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BusLine> getBusLines() {
        try {
            databaseDriver.connect();
            var busLines = databaseDriver.getBusLines();
            databaseDriver.disconnect();
            return busLines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stop> getStops() {
        try {
            databaseDriver.connect();
            var stops = databaseDriver.getAllStops();
            databaseDriver.disconnect();
            return stops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Route getRoute(BusLine busLine) {
        try {
            databaseDriver.connect();
            var stops = databaseDriver.getRouteForBusLine(busLine);
            databaseDriver.disconnect();
            return stops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the closest stop to a given coordinate (using Euclidean distance, not great circle distance)
     * @param latitude - North/South coordinate (positive is North, Negative is South) in degrees
     * @param longitude - East/West coordinate (negative is West, Positive is East) in degrees
     * @return the closest Stop
     */
    public Stop getClosestStop(double latitude, double longitude) {
        List<Stop> stops = null;
        //TODO: implement
        try {
            databaseDriver.connect();
            stops = databaseDriver.getAllStops();
            databaseDriver.disconnect();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        Stop closestStop = null;
        double shortestDistance = stops.get(0).distanceTo(latitude, longitude);
        for (Stop stop: stops){
            double currentDistance = stop.distanceTo(latitude, longitude);
            if(currentDistance<=shortestDistance){
                shortestDistance = currentDistance;
                closestStop = stop;
            }
        }
        return closestStop;
    }

    /**
     * Given two stop, a source and a destination, find the shortest (by distance) BusLine that starts
     * from source and ends at Destination.
     * @return Optional.empty() if no bus route visits both points
     * @throws IllegalArgumentException if either stop doesn't exist in the database
     */
    public Optional<BusLine> getRecommendedBusLine(Stop source, Stop destination) {
        //TODO: implement
        List<BusLine> busLines = null;
        try{
            busLines = databaseDriver.getBusLines();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        double shortestDistance = 100000.0;
        BusLine returnBusLine = null;
        for(BusLine busLine: busLines){
            Route route = busLine.getRoute();
            double currentDistance = 0.0;
            if (route.contains(source) && route.contains(destination)){
                currentDistance = countDistance(route, source, destination);
                if (currentDistance<shortestDistance){
                    returnBusLine = busLine;
                    shortestDistance = currentDistance;
                }
            }
        }
        if(returnBusLine==null){
            return Optional.empty();
        }
        return Optional.of(returnBusLine);
    }
    public double countDistance(Route route, Stop source, Stop destination){
        boolean finishCount = false;
        boolean count = false;
        int counter = 0;
        double distance = 0.0;
        while(!finishCount){
            Stop stop = route.getStops().get(counter);
            Stop nextStop = null;
            if(counter == route.getStops().size()-1){
                counter = -1;
            }
            nextStop = route.getStops().get(counter+1);
            if(stop.equals(source)){
                count = true;
            }
            if(count){
                distance += stop.distanceTo(nextStop);
                //System.out.println(String.valueOf(counter)+" "+String.valueOf(stop.distanceTo(nextStop)));
            }
            counter += 1;
            if(count && nextStop.equals(destination)){
                finishCount = true;
            }
        }
        return distance;
    }
}
