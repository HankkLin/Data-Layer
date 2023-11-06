package edu.virginia.sde.hw5;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


public class DatabaseDriver {
    private final String sqliteFilename;
    private Connection connection;

    public DatabaseDriver(Configuration configuration) {
        this.sqliteFilename = configuration.getDatabaseFilename();
    }

    public DatabaseDriver (String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    /**
     * Connect to a SQLite Database. This turns out Foreign Key enforcement, and disables auto-commits
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    /**
     * Commit all changes since the connection was opened OR since the last commit/rollback
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Rollback to the last commit, or when the connection was opened
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Ends the connection to the database
     */
    public void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Creates the three database tables Stops, BusLines, and Routes, with the appropriate constraints including
     * foreign keys, if they do not exist already. If they already exist, this method does nothing.
     * As a hint, you'll need to create Routes last, and Routes must include Foreign Keys to Stops and
     * BusLines.
     * @throws SQLException
     */
    public void createTables() throws SQLException {
        //TODO: implement
        Statement statement = connection.createStatement();
        String stops = "CREATE TABLE IF NOT EXISTS Stops (" +
                "ID INTEGER PRIMARY KEY," +
                "StopName TEXT," +
                "Latitude REAL," +
                "Longitude REAL)";
        String busLines = "CREATE TABLE IF NOT EXISTS BusLines (\n" +
                "ID INTEGER PRIMARY KEY," +
                "IsActive BOOLEAN," +
                "LongName TEXT," +
                "ShortName TEXT)";
        String route = "CREATE TABLE IF NOT EXISTS Routes (\n" +
                "ID INTEGER PRIMARY KEY," +
                "BusLineID INTEGER," +
                "StopID INTEGER," +
                "RouteOrder INTEGER," +
                "FOREIGN KEY (BusLineID) REFERENCES BusLines(ID) ON DELETE CASCADE," +
                "FOREIGN KEY (StopID) REFERENCES Stops(ID) ON DELETE CASCADE)";
        statement.executeUpdate(stops);
        statement.executeUpdate(busLines);
        statement.executeUpdate(route);
    }

    /**
     * Add a list of Stops to the Database. After adding all the stops, the changes will be committed. However,
     * if any SQLExceptions occur, this method will rollback and throw the exception.
     * @param stops - the stops to be added to the database
     */
    public void addStops(List<Stop> stops) throws SQLException {
        //TODO: implement
        String stopSQL = "INSERT OR IGNORE INTO Stops (ID, StopName, Latitude, Longitude) " +
                "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
            for (int i =0;i<stops.size();i++){
                int id = stops.get(i).getId();
                String StopName = stops.get(i).getName();
                double latitude = stops.get(i).getLatitude();
                double longitude = stops.get(i).getLongitude();

                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, StopName);
                preparedStatement.setDouble(3, latitude);
                preparedStatement.setDouble(4, longitude);

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    /**
     * Gets a list of all Stops in the database
     */
    public List<Stop> getAllStops() throws SQLException {
        List<Stop> stops = new ArrayList<>();
        //TODO: implement
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM stops";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            String stopName = resultSet.getString("StopName");
            double latitude = resultSet.getDouble("Latitude");
            double longitude = resultSet.getDouble("Longitude");

            Stop stop = new Stop(id, stopName, latitude, longitude);
            stops.add(stop);
        }
        return stops;
    }

    /**
     * Get a Stop by its ID number. Returns Optional.isEmpty() if no Stop matches the ID.
     */
    public Optional<Stop> getStopById(int stopId) throws SQLException {
        //TODO: implement
        String stopSQL = "SELECT * FROM Stops WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
        preparedStatement.setInt(1,stopId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            String stopName = resultSet.getString("StopName");
            double latitude = resultSet.getDouble("Latitude");
            double longitude = resultSet.getDouble("Longitude");

            Stop stop = new Stop(id, stopName, latitude, longitude);
            return Optional.of(stop);
        }
        return Optional.empty();
    }

    /**
     * Get all Stops whose name contains the substring (case-insensitive). For example, the parameter "Rice"
     * would return a List of Stops containing "Whitehead Rd @ Rice Hall"
     */
    public List<Stop> getStopsByName(String subString) throws SQLException {
        //TODO: implement
        List<Stop> stops = new ArrayList<>();
        String stopSQL = "SELECT * FROM Stops WHERE StopName LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
        preparedStatement.setString(1,"%"+subString+"%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            String stopName = resultSet.getString("StopName");
            double latitude = resultSet.getDouble("Latitude");
            double longitude = resultSet.getDouble("Longitude");

            Stop stop = new Stop(id, stopName, latitude, longitude);
            stops.add(stop);
        }
        return stops;
    }

    /**
     * Add BusLines and their Routes to the database, including their routes. This method should only be called after
     * Stops are added to the database via addStops, since Routes depends on the StopIds already being
     * in the database. If any SQLExceptions occur, this method will rollback all changes since
     * the method was called. This could happen if, for example, a BusLine contains a Stop that is not in the database.
     */
    public void addBusLines(List<BusLine> busLines) throws SQLException {
        //TODO: implement
        String stopSQL = "INSERT OR IGNORE INTO busLines (ID, IsActive, LongName, ShortName) " +
                "VALUES (?, ?, ?, ?)";
        String routeSQL = "INSERT OR IGNORE INTO Routes (ID, BusLineID, StopID, RouteOrder) " +
                "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
            for (int i =0;i<busLines.size();i++){
                int id = busLines.get(i).getId();
                boolean IsActive = busLines.get(i).isActive();
                String LongName = busLines.get(i).getLongName();
                String ShortName = busLines.get(i).getShortName();

                preparedStatement.setInt(1, id);
                preparedStatement.setBoolean(2, IsActive);
                preparedStatement.setString(3, LongName);
                preparedStatement.setString(4, ShortName);

                preparedStatement.executeUpdate();
            }
            //implement route
            int counter = 1;
            preparedStatement = connection.prepareStatement(routeSQL);
            for (int i =0;i<busLines.size();i++){
                Route route = busLines.get(i).getRoute();
                List<Stop> stops = route.getStops();
                for(int j = 0; j <stops.size();j++){

                    preparedStatement.setInt(1,counter);
                    counter +=1;
                    preparedStatement.setInt(2,busLines.get(i).getId());
                    preparedStatement.setInt(3,stops.get(j).getId());
                    preparedStatement.setInt(4,j);

                    preparedStatement.executeUpdate();
                }

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    /**
     * Return a list of all BusLines
     */
    public List<BusLine> getBusLines() throws SQLException {
        List<BusLine> busLines = new ArrayList<>();
        //TODO: implement
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM busLines";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            boolean isActive = resultSet.getBoolean("IsActive");
            String longName = resultSet.getString("LongName");
            String shortName = resultSet.getString("ShortName");

            BusLine busLine = new BusLine(id, isActive, longName, shortName);
            busLine.setRoute(getRouteForBusLine(busLine));
            busLines.add(busLine);
        }
        return busLines;
    }

    /**
     * Get a BusLine by its id number. Return Optional.empty() if no busLine is found
     */
    public Optional<BusLine> getBusLinesById(int busLineId) throws SQLException {
        //TODO: implement
        String stopSQL = "SELECT * FROM BusLines WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
        preparedStatement.setInt(1,busLineId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            boolean isActive = resultSet.getBoolean("IsActive");
            String longName = resultSet.getString("LongName");
            String shortName = resultSet.getString("ShortName");

            BusLine busLine = new BusLine(id, isActive, longName, shortName);
            busLine.setRoute(getRouteForBusLine(busLine));

            return Optional.of(busLine);
        }
        return Optional.empty();
    }

    /**
     * Get BusLine by its full long name (case-insensitive). Return Optional.empty() if no busLine is found.
     */
    public Optional<BusLine> getBusLineByLongName(String longName) throws SQLException {
        //TODO: implement
        String stopSQL = "SELECT * FROM BusLines WHERE LongName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
        preparedStatement.setString(1,longName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            boolean isActive = resultSet.getBoolean("IsActive");
            String stoplongName = resultSet.getString("LongName");
            String shortName = resultSet.getString("ShortName");

            BusLine busLine = new BusLine(id, isActive, stoplongName, shortName);
            busLine.setRoute(getRouteForBusLine(busLine));

            return Optional.of(busLine);
        }
        return Optional.empty();
    }

    /**
     * Get BusLine by its full short name (case-insensitive). Return Optional.empty() if no busLine is found.
     */
    public Optional<BusLine> getBusLineByShortName(String shortName) throws SQLException {
        //TODO: implement
        String stopSQL = "SELECT * FROM BusLines WHERE ShortName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(stopSQL);
        preparedStatement.setString(1,shortName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt("ID");
            boolean isActive = resultSet.getBoolean("IsActive");
            String longName = resultSet.getString("LongName");
            String stopshortName = resultSet.getString("ShortName");

            BusLine busLine = new BusLine(id, isActive, longName, stopshortName);
            busLine.setRoute(getRouteForBusLine(busLine));
            return Optional.of(busLine);
        }
        return Optional.empty();
    }

    /**
     * Get all BusLines that visit a particular stop
     */
    public List<BusLine> getBusLinesByStop(Stop stop) throws SQLException {
        //TODO: implement
        List<BusLine> busLines = new ArrayList<>();
        int stopID = stop.getId();
        String RouteSQL = "SELECT * FROM Routes WHERE StopID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(RouteSQL);
        preparedStatement.setInt(1,stopID);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int busID = resultSet.getInt(2);
            BusLine busLine = getBusLinesById(busID).orElse(null);
            busLines.add(busLine);
        }
        return busLines;
    }

    /**
     * Returns a BusLine's route as a List of stops *in-order*
     * @param busLine
     * @throws SQLException
     * @throws java.util.NoSuchElementException if busLine is not in the database
     */
    public Route getRouteForBusLine(BusLine busLine) throws SQLException {
        //TODO: implement
        List<Stop> stops = new ArrayList<>();
        int busLineID = busLine.getId();
        String stopsQuery = "SELECT * FROM Routes Where BusLineID = ? ORDER BY RouteOrder ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(stopsQuery);
        preparedStatement.setInt(1, busLineID);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int stopId = resultSet.getInt(3);
            Stop stop = getStopById(stopId).orElse(null);
            stops.add(stop);
        }
        Route route = new Route(stops);
        return route;
    }

    /**
     * Removes all data from the tables, leaving the tables empty (but still existing!). As a hint, delete the
     * contents of Routes first in order to avoid violating foreign key constraints.
     */
    public void clearTables() throws SQLException {
        //TODO: implement
        try {
            Statement statement = connection.createStatement();
            String removeRoute = "DELETE FROM Routes";
            String removeBusLines = "DELETE FROM BusLines";
            String removeStops = "DELETE FROM Stops";
            statement.executeUpdate(removeRoute);
            statement.executeUpdate(removeBusLines);
            statement.executeUpdate(removeStops);
        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

}
