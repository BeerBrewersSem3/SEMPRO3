package beerbrewers.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/sensor")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    private SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<Sensor> getSensors() {
        return sensorService.getSensors();
    }

    @PutMapping(path = "{sensorId}")
    public void updateSensorValue(
            @PathVariable("sensorId") Long sensorId,
            @RequestParam(required = false) float currentSensorValue) {
        sensorService.updateSensorValue(sensorId, currentSensorValue);
    }
}
