package edu.virginia.sde.hw5;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.verify;

public class BusLineTestReader {
    Configuration configuration = new Configuration();
    BusLineReader busLineReader = new BusLineReader(configuration);
    @Test
    public void getStopsTest(){
        List x = busLineReader.getBusLines();
        for (int i = 0; i < x.size();i++){
            System.out.println(x.get(i));
        }
    }

}
