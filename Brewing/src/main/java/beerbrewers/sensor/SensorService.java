package beerbrewers.sensor;

import beerbrewers.worker.Worker;
import beerbrewers.worker.WorkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    @Transactional
    public void updateSensorValue(Long sensorId, float currentSensorValue) {
        boolean exists = sensorRepository.existsById(sensorId);
        if (!exists) {
            throw new IllegalStateException("Sensor with id '" + sensorId + "' does not exist.");
        }
        Sensor sensor = sensorRepository.findBySensorId(sensorId);
        sensor.setCurrentSensorValue(currentSensorValue);
    }
}
