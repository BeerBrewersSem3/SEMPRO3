package beerbrewers.websocket;

import beerbrewers.opcua.OpcUaDashboardService;
import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketDataController {

    @MessageMapping("/sensor/stateCurrent")
    @SendTo("/sensor/data/stateCurrent")
    public String message(String message) {
        return message;
    }
}
