package beerbrewers.opcua;

import beerbrewers.websocket.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OpcUaDashboardService implements OpcUaNodeObserver {

    private static final Logger logger = LoggerFactory.getLogger(OpcUaDashboardService.class);
    private final WebsocketService websocketService;
    private Map<OpcUaNode, String> currentNodeValueMap = new ConcurrentHashMap<>();

    private List<OpcUaNode> subscribedNodes = List.of(
            OpcUaNode.TEMPERATURE,
            OpcUaNode.REL_HUMIDITY,
            OpcUaNode.VIBRATION,
            OpcUaNode.CURRENT_BATCH_ID,
            OpcUaNode.CURRENT_BATCH_AMOUNT,
            OpcUaNode.CUR_MACH_SPEED,
            OpcUaNode.PROD_PRODUCED,
            OpcUaNode.PROD_GOOD,
            OpcUaNode.PROD_DEFECTIVE_COUNT,
            OpcUaNode.MAINTENANCE_COUNTER,
            OpcUaNode.MAINTENANCE_TRIGGER,
            OpcUaNode.BARLEY,
            OpcUaNode.MALT,
            OpcUaNode.HOPS,
            OpcUaNode.WHEAT,
            OpcUaNode.YEAST,
            OpcUaNode.FILLING_INVENTORY
    );

    @Autowired
    public OpcUaDashboardService(WebsocketService websocketService){
        this.websocketService = websocketService;
    }


    @Override
    public void onNodeUpdate(OpcUaNode node, String newState) {
        currentNodeValueMap.put(node, newState);
        websocketService.send(node.getName(), newState);
        logger.debug("WebSocket update: " + node.getName() + " has new value of: " + newState);
    }
    @Override
    public List<OpcUaNode> getSubscribedNodes() {
        return subscribedNodes;
    }

    public Map<OpcUaNode, String> getCurrentNodeValueMap() {
        return currentNodeValueMap;
    }

}
