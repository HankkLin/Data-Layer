package edu.virginia.sde.hw5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class DatabaseDriverTest {
    Configuration configuration = new Configuration();
    DatabaseDriver databaseDriver = new DatabaseDriver(configuration);
    StopReader stopReader = new StopReader(configuration);
    BusLineReader busLineReader = new BusLineReader(configuration);

    @BeforeEach
    public void setUp(){
        try {
            databaseDriver.connect();
            databaseDriver.createTables();
            databaseDriver.addStops(stopReader.getStops());
            databaseDriver.addBusLines(busLineReader.getBusLines());
            databaseDriver.commit();
        } catch (SQLException e) {
            System.out.println("Setup Error");
            throw new RuntimeException(e);
        }
    }
    @AfterEach
    void disconnect(){
        try {
            databaseDriver.disconnect();
        } catch (SQLException e){
            System.out.println("Disconnect Error");
            throw new RuntimeException(e);
        }

    }
    @Test
    void getAllStopsTest(){
        List x = null;
        try{
            x = databaseDriver.getAllStops();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < x.size();i++){
            System.out.println(x.get(i));
        }

    }
    @Test
    void getStopByIdTest(){
        try {
            System.out.println(databaseDriver.getStopById(4235114));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void getStopsByNameTest(){
        List x = null;
        try {
            x = databaseDriver.getStopsByName("a");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < x.size();i++){
            System.out.println(x.get(i));
        }
    }
    @Test
    void getBusLinesTest(){
        List x = null;
        try {
            x = databaseDriver.getBusLines();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < x.size();i++){
            System.out.println(x.get(i));
        }
    }
    @Test
    void getBusLinesByIdTest(){
        try {
            System.out.println(databaseDriver.getBusLinesById(4013472));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBusLineByLongNameTest(){
        try {
            System.out.println(databaseDriver.getBusLineByLongName("Lovingston CONNECT"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getBusLineByShortNameTest(){
        try {
            System.out.println(databaseDriver.getBusLineByShortName("BLUE"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void getBusLinesByStopTest(){
        try {
            System.out.println(databaseDriver.getBusLinesByStop(stopReader.getStops().get(5)));
            System.out.println(databaseDriver.getBusLinesByStop(stopReader.getStops().get(5)).size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void getRouteForBusLineTest(){
        try {
            System.out.println(databaseDriver.getRouteForBusLine(busLineReader.getBusLines().get(1)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearTablesTest(){
        try{
            databaseDriver.clearTables();
            System.out.println(databaseDriver.getBusLines());
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


//    @Test
//    void printSpecific(){
//        try {
//
//        } catch (Exception e){
//            System.out.println("Something wrong happen.");
//        }
//    }
}
