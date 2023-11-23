package beerbrewers.sensorreading;

import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.operation.Operation;
import beerbrewers.operation.OperationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Service
public class SensorReadingService implements OpcUaNodeObserver {
    private final SensorReadingRepository sensorReadingRepository;
    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;

    private HashMap<Long, Float> currentSensorValuesMap;

    private final OperationService operationService;

    @Autowired
    public SensorReadingService(
            SensorReadingRepository sensorReadingRepository,
            OpcUaNodeUpdateManager opcUaNodeUpdateManager,
            OperationService operationService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.operationService = operationService;
        this.currentSensorValuesMap = new HashMap<>();
    }

    @PostConstruct
    public void initializeSubscription() {
        opcUaNodeUpdateManager.addObserver(OpcuaNodes.REL_HUMIDITY, this);
        opcUaNodeUpdateManager.addObserver(OpcuaNodes.TEMPERATURE, this);
        opcUaNodeUpdateManager.addObserver(OpcuaNodes.VIBRATION, this);
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
        for (Map.Entry<Long, Float> set : currentSensorValuesMap.entrySet()) {
            addNewSensorReading(
                    new SensorReading(
                            new SensorReadingId(operationService.getCurrentRunningOperation().getOperationId(), set.getKey()),
                            new Timestamp(System.currentTimeMillis()),
                            set.getValue()));
        }
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        currentSensorValuesMap.put(node.getDatabaseId(), Float.valueOf(newState));
    }
}
