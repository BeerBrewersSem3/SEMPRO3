package beerbrewers;

import beerbrewers.operation.Operation;
import beerbrewers.sensor.Sensor;
import beerbrewers.sensor.SensorRepository;
import beerbrewers.sensor.SensorService;
import beerbrewers.sensorreading.SensorReading;
import beerbrewers.sensorreading.SensorReadingRepository;
import beerbrewers.sensorreading.SensorReadingService;
import beerbrewers.worker.Worker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SensorReadingTest {


    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingService sensorReadingService;
    private SensorReading sensorReading;


    @Autowired
    SensorReadingTest(SensorReadingRepository sensorReadingRepository, SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
        this.sensorReadingRepository = sensorReadingRepository;
//        Worker worker = new Worker("Unit test", "Test Unit");
//        Operation operation = new Operation(worker);
//        Sensor sensor = new Sensor("Unit test sensor", 6, "::Program:Test.unitTest");
//        this.sensorReading = new SensorReading(operation, sensor, new Timestamp(System.currentTimeMillis()), 0);
    }

    @Test
    public void getAllSensorReadings() {
        List<SensorReading> allSensorReadings = this.sensorReadingService.getSensorReadings();
        assertNotNull(allSensorReadings);
        assertTrue(allSensorReadings.size() > 0);

    }

}
