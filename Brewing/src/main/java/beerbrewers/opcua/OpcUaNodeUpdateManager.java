package beerbrewers.opcua;

import beerbrewers.batch.BatchService;
import beerbrewers.machine.MachineService;
import beerbrewers.sensorreading.SensorReadingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OpcUaNodeUpdateManager {

    private final MachineService machineService;
    private final BatchService batchService;
    private final OpcUaDashboardService opcUaDashboardService;
    private final SensorReadingService sensorReadingService;

    private static final Logger logger = LoggerFactory.getLogger(OpcUaNodeUpdateManager.class);

    private Map<OpcuaNodes, List<OpcUaNodeObserver>> observers = new ConcurrentHashMap<>();

    @Autowired
    public OpcUaNodeUpdateManager(MachineService machineService,
                                  BatchService batchService,
                                  OpcUaDashboardService opcUaDashboardService,
                                  SensorReadingService sensorReadingService) {
        this.machineService = machineService;
        this.batchService = batchService;
        this.opcUaDashboardService = opcUaDashboardService;
        this.sensorReadingService = sensorReadingService;
    }

    @PostConstruct
    public void initializeSubscribers(){
        addObserver(machineService);
        addObserver(batchService);
        addObserver(opcUaDashboardService);
        addObserver(sensorReadingService);
    }

    public void addObserver(OpcUaNodeObserver opcUaNodeObserver) {
        opcUaNodeObserver.getSubscribedNodes().forEach(opcuaNode -> {
            addObserver(opcuaNode, opcUaNodeObserver);
        });
    }

    public void addObserver(OpcuaNodes node, OpcUaNodeObserver observer) {
        observers.computeIfAbsent(node, k -> new ArrayList<>()).add(observer);
    }


    public void notifyObservers(OpcuaNodes node, String newState) {
        List<OpcUaNodeObserver> nodeObservers = observers.getOrDefault(node, Collections.emptyList());
        for (OpcUaNodeObserver observer : nodeObservers) {
            observer.onNodeUpdate(node, newState);
            logger.debug("OpcUaNodeUpdateManager notified observer: " + observer.getClass().getSimpleName() + " of node: " + node.getName() + " with new state: " + newState);
        }
    }
}
