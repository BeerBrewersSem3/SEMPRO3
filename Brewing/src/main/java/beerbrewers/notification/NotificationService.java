package beerbrewers.notification;
import beerbrewers.opcua.OpcUaNodeObserver;
import beerbrewers.opcua.OpcUaNode;
import beerbrewers.websocket.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService implements OpcUaNodeObserver {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureService.class);
    private final WebsocketService websocketService;
    private HashMap<OpcUaNode, String> nodeValueMap;
    private final TemperatureService temperatureService;
    private final InventoryService inventoryService;
    private boolean fillingInventory = false;
    private List<OpcUaNode> subscribeNodes = List.of(
            OpcUaNode.STATE_CURRENT,
            OpcUaNode.TEMPERATURE,
            OpcUaNode.BARLEY,
            OpcUaNode.MALT,
            OpcUaNode.WHEAT,
            OpcUaNode.HOPS,
            OpcUaNode.YEAST,
            OpcUaNode.FILLING_INVENTORY
    );
    private List<OpcUaNode> siloNodes = List.of(
            OpcUaNode.BARLEY,
            OpcUaNode.MALT,
            OpcUaNode.WHEAT,
            OpcUaNode.HOPS,
            OpcUaNode.YEAST
    );

    @Autowired
    public NotificationService(WebsocketService websocketService, TemperatureService temperatureService, InventoryService inventoryService) {
        this.websocketService = websocketService;
        this.temperatureService = temperatureService;
        this.inventoryService = inventoryService;
        this.nodeValueMap = new HashMap<>();
    }

    @Override
    public void onNodeUpdate(OpcUaNode node, String newState) {
        nodeValueMap.put(node, newState);

        if(Objects.equals(node, OpcUaNode.FILLING_INVENTORY)){
            fillingInventory = Boolean.parseBoolean(newState);
        }

        if(siloNodes.contains(node) && !fillingInventory){
            inventoryService.inventoryWarning(node, newState);
        }

        //Test if statement
        if(node == OpcUaNode.STATE_CURRENT){
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

    @Override
    public List<OpcUaNode> getSubscribedNodes() {
        return subscribeNodes;
    }
}


