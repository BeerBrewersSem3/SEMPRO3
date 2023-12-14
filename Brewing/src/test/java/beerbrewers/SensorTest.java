package beerbrewers;

import beerbrewers.operation.Operation;
import beerbrewers.sensor.Sensor;
import beerbrewers.sensor.SensorRepository;
import beerbrewers.sensor.SensorService;
import beerbrewers.sensorreading.SensorReading;
import beerbrewers.sensorreading.SensorReadingRepository;
import beerbrewers.sensorreading.SensorReadingService;
import beerbrewers.worker.Worker;
import beerbrewers.worker.WorkerRepository;
import beerbrewers.worker.WorkerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SensorTest {


    private final SensorRepository sensorRepository;
    private final SensorService sensorService;
    private Sensor sensor;
    private long startTime, endTime;


    @Autowired
    SensorTest(SensorRepository sensorRepository, SensorService sensorService){
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
        this.sensor = new Sensor("Unit test sensor", 6, "::Program:Test.unitTest");
    }

    @Test
    public void getAllSensors() {
        List<Sensor> allSensors = this.sensorService.getSensors();
        assertNotNull(allSensors);
    }

    @Test
    public void getSpecificSensor() {
        startTime = System.currentTimeMillis();
        Sensor specificSensor1 = this.sensorService.getSensor(1L);
        assertNotNull(specificSensor1);

        Sensor specificSensor2 = this.sensorService.getSensor(2L);
        assertNotNull(specificSensor2);

        Sensor specificSensor3 = this.sensorService.getSensor(3L);
        assertNotNull(specificSensor3);

        Sensor specificSensor4 = this.sensorService.getSensor(4L);
        assertNotNull(specificSensor4);
        endTime = System.currentTimeMillis();
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(endTime - startTime);

        //ResetMachineState, StopMachine,
    }
}