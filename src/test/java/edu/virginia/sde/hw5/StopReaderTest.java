package edu.virginia.sde.hw5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class StopReaderTest {
    Configuration configuration = new Configuration();
    StopReader stopReader = new StopReader(configuration);
    @Test
    public void getStopsTest(){
        List x = stopReader.getStops();
        for (int i = 0; i < x.size();i++){
            System.out.println(x.get(i));
        }
    }
}
