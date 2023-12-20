package beerbrewers.sensorreading;

import beerbrewers.batch.BatchService;
import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNode;
import beerbrewers.sensor.Sensor;
import beerbrewers.sensor.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Service
@Lazy
public class SensorReadingService implements OpcUaNodeObserver {
    private final SensorReadingRepository sensorReadingRepository;

    private HashMap<Long, Float> currentSensorValuesMap;

    private final BatchService batchService;

    private final SensorService sensorService;

    private List<Sensor> sensorList;

    private List<OpcUaNode> subscribedNodes = List.of(
            OpcUaNode.REL_HUMIDITY,
            OpcUaNode.TEMPERATURE,
            OpcUaNode.VIBRATION
    );

    @Autowired
    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository,
            BatchService batchService, SensorService sensorService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.batchService = batchService;
        this.sensorService = sensorService;
        this.currentSensorValuesMap = new HashMap<>();
        this.sensorList = new ArrayList<>();
    }

    public void getAllSensors() {
        Sensor sensor1 = sensorService.getSensor(1L);
        Sensor sensor2 = sensorService.getSensor(2L);
        Sensor sensor3 = sensorService.getSensor(3L);

        sensorList.add(sensor1);
        sensorList.add(sensor2);
        sensorList.add(sensor3);
    }

    public List<SensorReading> getSensorReadings() {
        return sensorReadingRepository.findAll();
    }
    public void addNewSensorReading(SensorReading sensorReading) {
        sensorReadingRepository.save(sensorReading);
    }

    /**
     * This method has the @Scheduled annotation, which means that Spring Boot calls the method every 'N' seconds.
     * Note that the class has the @EnableScheduling annotation, to make Spring aware that it must look for
     * scheduling tasks within.
     */
    @Scheduled(fixedDelay = 10000)
    public void updateSensorReadings() {
        if (batchService.getCurrentBatch() != null) {
            if (sensorList.isEmpty()) {
                getAllSensors();
            }
            for (Map.Entry<Long, Float> set : currentSensorValuesMap.entrySet()) {
                addNewSensorReading(
                        new SensorReading(
                                batchService.getCurrentBatch(),
                                sensorList.get((int) (set.getKey()-1)),
                                new Timestamp(System.currentTimeMillis()),
                                set.getValue()));
            }
        }
    }

    @Override
    public void onNodeUpdate(OpcUaNode node, String newState) {
        currentSensorValuesMap.put(node.getDatabaseId(), Float.valueOf(newState));
    }

    @Override
    public List<OpcUaNode> getSubscribedNodes() {
        return subscribedNodes;
    }

    public SensorReading getSensorReading(Long sensorReadingId) {
        return sensorReadingRepository.findBySensorReadingId(sensorReadingId);
    }
}
