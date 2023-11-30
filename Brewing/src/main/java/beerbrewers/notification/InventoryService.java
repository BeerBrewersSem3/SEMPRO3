package beerbrewers.notification;

import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.websocket.WebsocketService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final WebsocketService websocketService;

    public InventoryService(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }

    public void barleyWarning(OpcuaNodes node, String newState){
        float barley = Float.parseFloat(newState);

    }
}
