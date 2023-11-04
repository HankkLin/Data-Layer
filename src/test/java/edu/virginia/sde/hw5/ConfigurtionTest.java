package edu.virginia.sde.hw5;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigurtionTest {
    Configuration configuration = new Configuration();

    @Test
    public void parseJsonConfigFileTest(){
        URL stop = null;
        URL line = null;
        try{
            stop = new URL("https://www.cs.virginia.edu/~pm8fc/busses/stops.json");
            line = new URL("https://www.cs.virginia.edu/~pm8fc/busses/lines.json");

        }catch (MalformedURLException exception){
        }

        assertEquals(stop,configuration.getBusStopsURL());
        assertEquals(line,configuration.getBusLinesURL());
        assertEquals("bus_stops.sqlite",configuration.getDatabaseFilename());

    }
}
