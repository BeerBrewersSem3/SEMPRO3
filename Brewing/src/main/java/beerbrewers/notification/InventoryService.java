package beerbrewers.notification;

import beerbrewers.opcua.OpcUaNode;
import beerbrewers.websocket.WebsocketService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final WebsocketService websocketService;

    public InventoryService(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }

    public void inventoryWarning(OpcUaNode node, String newState) {
        switch (node) {
            case BARLEY -> {
                float barley = Float.parseFloat(newState);
                float procent = (float) Math.floor((barley) / 35000 * 100);
                if (procent < 50 && procent > 10) {
                    String message = "Warning - Barley running low. Current stock ";
                    websocketService.sendNotification(node, procent + "%", message);
                } else if (procent < 10){
                String message = "Critical stock - Barley inventory nearly empty";
                websocketService.sendNotification(node,procent + "%", message);
            }
        }
            case YEAST -> {
                float yeast = Float.parseFloat(newState);
                float procent = (float) Math.floor((yeast) / 35000 * 100);
                if (procent < 50 && procent > 10) {
                    String message = "Warning - Yeast running low. Current stock ";
                    websocketService.sendNotification(node, procent + "%", message);
                } else if (procent < 10){
                String message = "Critical - Yeast inventory nearly empty";
                websocketService.sendNotification(node,procent + "%", message);
            }
        }
            case HOPS -> {
                float hops = Float.parseFloat(newState);
                float procent = (float) Math.floor((hops) / 35000 * 100);
                if (procent < 50 && procent > 10) {
                    String message = "Warning - Hops running low. Current stock ";
                    websocketService.sendNotification(node, procent + "%", message);
                } else if (procent < 10){
                    String message = "Critical - Hops inventory nearly empty";
                    websocketService.sendNotification(node,procent + "%", message);
            }
        }
            case MALT -> {
                float malt = Float.parseFloat(newState);
                float procent = (float) Math.floor((malt) / 35000 * 100);
                if (procent < 50 && procent > 10) {
                    String message = "Warning - Malt running low. Current stock ";
                    websocketService.sendNotification(node, procent + "%", message);
                } else if (procent < 10){
                    String message = "Critical - Malt inventory nearly empty";
                    websocketService.sendNotification(node,procent + "%", message);
            }
        }

            case WHEAT -> {
                float wheat = Float.parseFloat(newState);
                float procent = (float) Math.floor((wheat) / 35000 * 100);
                if (procent < 50 && procent > 10) {
                    String message = "Warning - Wheat is running low. Current stock";
                    websocketService.sendNotification(node, procent + "%", message);
                } else if (procent < 10){
                    String message = "Critical - Wheat inventory nearly empty";
                    websocketService.sendNotification(node,procent + "%", message);
                }
            }
        }
    }
}
