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
        float procent = (float) Math.floor((barley)/35000*100);
        if(procent < 90) {
            String message = "Warning - Barley is running low. Current stock: ";
            websocketService.sendNotification(node, message);
        }

    }
}
