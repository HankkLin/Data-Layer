package edu.virginia.sde.hw5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class BusLineServiceTest {
    Configuration configuration = new Configuration();
    DatabaseDriver databaseDriver = new DatabaseDriver(configuration);
    BusLineService busLineService = new BusLineService(databaseDriver);

    @BeforeEach
    public void setUp() {
        try {
            databaseDriver.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void after() {
        try {
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getClosestStopTest() {
        Stop stop = busLineService.getClosestStop(38.043246, -78.515373);
        System.out.println(stop);
    }

    @Test
    public void returnDistanceTest() {
        try {
            BusLine busLine = databaseDriver.getBusLinesById(4013468).orElse(null);
            Route route = databaseDriver.getRouteForBusLine(busLine);
            Stop stop1 = databaseDriver.getStopById(4267022).orElse(null);
            Stop stop2 = databaseDriver.getStopById(4267060).orElse(null);
            double x = busLineService.countDistance(route, stop1, stop2);
            System.out.println(x);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void optimalTest(){
        try {
            Stop stop1 = databaseDriver.getStopById(4267002).orElse(null);
            Stop stop2 = databaseDriver.getStopById(4267032).orElse(null);
            System.out.println(busLineService.getRecommendedBusLine(stop1, stop2));
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}