package beerbrewers.opcua;

import beerbrewers.websocket.WebsocketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcUaDashboardService implements OpcUaNodeObserver{

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private static final Logger logger = LoggerFactory.getLogger(OpcUaDashboardService.class);
    private final WebsocketService websocketService;

    @Autowired
    public OpcUaDashboardService(OpcUaNodeUpdateManager opcUaNodeUpdateManager, WebsocketService websocketService){
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.websocketService = websocketService;
    }

    @PostConstruct
    public void initialize(){
        List<OpcuaNodes> nodesToSubscribe = List.of(
            OpcuaNodes.STATE_CURRENT,
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

        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.info("OpcuaDashboardService subscribed to node: " + node.getName());
        });
    }
    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        websocketService.send(node.getName(), newState);
        logger.info(node.getName() + " has new value of: " + newState);
    }
}
