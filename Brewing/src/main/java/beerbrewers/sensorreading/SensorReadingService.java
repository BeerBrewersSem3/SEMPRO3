package beerbrewers.sensorreading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorReadingService {
    private final SensorReadingRepository sensorReadingRepository;

    @Autowired
    public SensorReadingService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public List<SensorReading> getSensorReadings() {
        return sensorReadingRepository.findAll();
    }


    public void addNewSensorReading(SensorReading sensorReading) {
        sensorReadingRepository.save(sensorReading);
    }
}
