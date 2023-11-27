package beerbrewers.notification;

import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.websocket.WebsocketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NotificationService implements OpcUaNodeObserver {

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private static final Logger logger = LoggerFactory.getLogger(TemperatureService.class);
    private final WebsocketService websocketService;
    private HashMap<OpcuaNodes, String> nodeValueMap;
    private final TemperatureService temperatureService;

    @Autowired
    public NotificationService(OpcUaNodeUpdateManager opcUaNodeUpdateManager, WebsocketService websocketService, TemperatureService temperatureService) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.websocketService = websocketService;
        this.temperatureService = temperatureService;
        this.nodeValueMap = new HashMap<>();
    }

    @PostConstruct
    public void initializeSubscription() {
        List<OpcuaNodes> nodesToSubscribe = List.of(
                OpcuaNodes.STATE_CURRENT,
                OpcuaNodes.TEMPERATURE
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.info("TemperatureService subscribed to node: " + node.getName());
        });
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        nodeValueMap.put(node, newState);

        //Test if statement
        if(node == OpcuaNodes.STATE_CURRENT){
            System.out.println(newState);
            temperatureService.temperatureWarning(node, newState);
        }
        //Future switch case here:

//        switch(node){
//            case TEMPERATURE:
//                temp.temperatureWarning();
//                break;
//            case BATCH
//
//        }
    }
}

