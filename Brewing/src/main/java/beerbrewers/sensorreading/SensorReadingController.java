package beerbrewers.sensorreading;

import beerbrewers.operation.Operation;
import beerbrewers.sensor.Sensor;
import beerbrewers.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/sensorreading")
public class SensorReadingController {
    private final SensorReadingService sensorReadingService;

    @Autowired
    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @GetMapping
    public List<SensorReading> getSensorReadings() {
        return sensorReadingService.getSensorReadings();
    }

    @GetMapping(path = "{sensorReadingId}")
    public SensorReading findBySensorReadingId(@PathVariable("sensorReadingId") Long sensorReadingId) {
        return sensorReadingService.getSensorReading(sensorReadingId);
    }

    @PostMapping
    public void saveSensorReading(@RequestBody SensorReading sensorReading) {
        sensorReadingService.addNewSensorReading(sensorReading);
    }

}
