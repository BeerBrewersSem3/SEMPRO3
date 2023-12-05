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

<<<<<<< Updated upstream
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
=======

    private static final Logger logger = LoggerFactory.getLogger(OpcUaDashboardService.class);
    private final WebsocketService websocketService;
    private Map<OpcuaNodes, String> currentNodeValueMap = new ConcurrentHashMap<>();
    private Map<OpcuaNodes, String> currentBatchValues = new ConcurrentHashMap<>();
    private List<OpcuaNodes> subscribedNodes = List.of(
>>>>>>> Stashed changes
            OpcuaNodes.TEMPERATURE,
            OpcuaNodes.REL_HUMIDITY,
            OpcuaNodes.VIBRATION,
            OpcuaNodes.CURRENT_BATCH_ID,
            //OpcuaNodes.CURRENT_BATCH_AMOUNT,
            OpcuaNodes.CUR_MACH_SPEED,
            OpcuaNodes.PROD_PRODUCED,
            OpcuaNodes.PROD_PROCESSED_COUNT,
            OpcuaNodes.PROD_DEFECTIVE_COUNT
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.info("OpcuaDashboardService subscribed to node: " + node.getName());
        });
    }
    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
<<<<<<< Updated upstream
=======
        if(currentBatchValues.containsKey(node)){
            newState = String.valueOf(Integer.parseInt(newState)+Integer.parseInt(currentBatchValues.get(node)));
        }
        currentNodeValueMap.put(node, newState);
>>>>>>> Stashed changes
        websocketService.send(node.getName(), newState);
        logger.debug(node.getName() + " has new value of: " + newState);
    }
<<<<<<< Updated upstream
=======

    @Override
    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribedNodes;
    }

    public Map<OpcuaNodes, String> getCurrentNodeValueMap() {
        return currentNodeValueMap;
    }

    public void setCurrentBatchNodeValue(OpcuaNodes node, String value) {
        this.currentBatchValues.put(node, value);
    }
>>>>>>> Stashed changes
}
