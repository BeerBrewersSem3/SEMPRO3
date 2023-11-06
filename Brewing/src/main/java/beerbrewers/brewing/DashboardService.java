package beerbrewers.brewing;


import beerbrewers.BrewMaster9000.opcua.OpcUaNodeObserver;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodeUpdateManager;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodes;
import beerbrewers.brewing.websocket.WebSocketService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DashboardService implements OpcUaNodeObserver {

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private Map<OpcUaNodes,String> dashboardElements = new ConcurrentHashMap<>();

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    public DashboardService(OpcUaNodeUpdateManager opcUaNodeUpdateManager) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
    }
    @PostConstruct
    public void initialize() {
        List<OpcUaNodes> nodesToSubscribe = List.of(
                OpcUaNodes.STATE_CURRENT,
                OpcUaNodes.MACH_SPEED,
                OpcUaNodes.PROD_DEFECTIVE_COUNT,
                OpcUaNodes.PROD_PROCESSED_COUNT,
                OpcUaNodes.STOP_REASON,
                OpcUaNodes.CNTRL_CMD
        );

        //OpcUaNodes.BATCH_ID,
        //OpcUaNodes.BATCH_QTY,
        //OpcUaNodes.HUMIDITY,
        //OpcUaNodes.TEMPERATURE,
        //OpcUaNodes.VIBRATION
        //OpcUaNodes.CUR_MACH_SPEED,
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            System.out.println("Subscribed from DashboardService to " + node.getName());
        });

    }
    @Override
    public void onNodeUpdate(OpcUaNodes node, String newState) {
        System.out.println("Node: " + node.getName() + " has a new value: " + dashboardElements.get(node) + " -> " + newState);
        dashboardElements.put(node, newState);
        sendWebSocketUpdate(node, newState);
    }

    private void sendWebSocketUpdate(OpcUaNodes node, String newState) {
        System.out.println("Sending websocket update from DashboardService to " + node.getName() + ": " + newState);
        webSocketService.updateNodeState(node.getName(), newState);
    }
    public String getCurrentNodeValue(OpcUaNodes node) {
        return dashboardElements.get(node);
    }
}

