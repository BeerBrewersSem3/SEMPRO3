package beerbrewers.opcua;

import beerbrewers.websocket.WebsocketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OpcUaDashboardService implements OpcUaNodeObserver {

    private static final Logger logger = LoggerFactory.getLogger(OpcUaDashboardService.class);
    private final WebsocketService websocketService;
    private Map<OpcuaNodes, String> currentNodeValueMap = new ConcurrentHashMap<>();

    private List<OpcuaNodes> subscribedNodes = List.of(
            OpcuaNodes.TEMPERATURE,
            OpcuaNodes.REL_HUMIDITY,
            OpcuaNodes.VIBRATION,
            OpcuaNodes.CURRENT_BATCH_ID,
            OpcuaNodes.CURRENT_BATCH_AMOUNT,
            OpcuaNodes.CUR_MACH_SPEED,
            OpcuaNodes.PROD_PRODUCED,
            OpcuaNodes.PROD_PROCESSED_COUNT,
            OpcuaNodes.PROD_DEFECTIVE_COUNT,
            OpcuaNodes.MAINTENANCE_COUNTER,
            OpcuaNodes.MAINTENANCE_TRIGGER,
            OpcuaNodes.BARLEY,
            OpcuaNodes.MALT,
            OpcuaNodes.HOPS,
            OpcuaNodes.WHEAT,
            OpcuaNodes.YEAST
    );

    @Autowired
    public OpcUaDashboardService(WebsocketService websocketService){
        this.websocketService = websocketService;
    }


    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        currentNodeValueMap.put(node, newState);
        websocketService.send(node.getName(), newState);
        logger.debug("WebSocket update: " + node.getName() + " has new value of: " + newState);
    }

    @Override
    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribedNodes;
    }

    public Map<OpcuaNodes, String> getCurrentNodeValueMap() {
        return currentNodeValueMap;
    }

}
