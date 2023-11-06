package beerbrewers.brewing.Monitoring;


import beerbrewers.BrewMaster9000.opcua.OpcUaNodeObserver;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodeUpdateManager;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodes;
import beerbrewers.brewing.websocket.WebSocketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("FieldMayBeFinal")
@Service
public class DashboardService implements OpcUaNodeObserver {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private  Map<OpcUaNodes,String> dashboardElements = new ConcurrentHashMap<>();

    private final WebSocketService webSocketService;
    @Autowired
    public DashboardService(OpcUaNodeUpdateManager opcUaNodeUpdateManager, WebSocketService webSocketService) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.webSocketService = webSocketService;
    }
    @PostConstruct
    public void initialize() {
        List<OpcUaNodes> nodesToSubscribe = List.of(
                OpcUaNodes.STATE_CURRENT,
                OpcUaNodes.MACH_SPEED_READ,
                OpcUaNodes.PROD_DEFECTIVE_COUNT,
                OpcUaNodes.PROD_PROCESSED_COUNT,
                OpcUaNodes.STOP_REASON,
                OpcUaNodes.CNTRL_CMD,
                OpcUaNodes.CURRENT_BATCH_ID,
                OpcUaNodes.BATCH_QTY,
                OpcUaNodes.REL_HUMIDITY,
                OpcUaNodes.TEMPERATURE,
                OpcUaNodes.VIBRATION,
                OpcUaNodes.CUR_MACH_SPEED
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.debug("DashboardService subscribed to node: " + node.getName());
        });

    }
    @Override
    public void onNodeUpdate(OpcUaNodes node, String newState) {
        logger.debug("DashboardService received update from node: " + node.getName() + " with new state: " + newState);
        dashboardElements.put(node, newState);
        sendWebSocketUpdate(node, newState);
    }

    private void sendWebSocketUpdate(OpcUaNodes node, String newState) {
        logger.debug("Sending websocket update from DashboardService to " + node.getName() + ": " + newState);
        webSocketService.updateNodeState("/sensor/data/"+node.getName(), newState);
    }
    public String getCurrentNodeValue(OpcUaNodes node) {
        return dashboardElements.get(node);
    }
}

