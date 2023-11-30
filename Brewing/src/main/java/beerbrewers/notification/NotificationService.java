package beerbrewers.notification;
import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.websocket.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NotificationService implements OpcUaNodeObserver {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureService.class);
    private final WebsocketService websocketService;
    private HashMap<OpcuaNodes, String> nodeValueMap;
    private final TemperatureService temperatureService;
    private final InventoryService inventoryService;
    private List<OpcuaNodes> subscribeNodes = List.of(
            OpcuaNodes.STATE_CURRENT,
            OpcuaNodes.TEMPERATURE,
            OpcuaNodes.BARLEY
    );

    @Autowired
    public NotificationService(WebsocketService websocketService, TemperatureService temperatureService, InventoryService inventoryService) {
        this.websocketService = websocketService;
        this.temperatureService = temperatureService;
        this.inventoryService = inventoryService;
        this.nodeValueMap = new HashMap<>();
    }

    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        nodeValueMap.put(node, newState);

        //Test if statement
        if(node == OpcuaNodes.STATE_CURRENT){
            System.out.println(newState);
            temperatureService.temperatureWarning(node, newState);
        }

        if(node == OpcuaNodes.BARLEY){
            System.out.println(newState);
            inventoryService.barleyWarning(node, newState);
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

    @Override
    public List<OpcuaNodes> getSubscribedNodes() {
        return subscribeNodes;
    }
}


