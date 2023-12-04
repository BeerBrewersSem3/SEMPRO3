package beerbrewers.websocket;

import beerbrewers.opcua.OpcUaDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    private final WebsocketService websocketService;
    private final OpcUaDashboardService opcUaDashboardService;

    @Autowired
    public WebsocketController(WebsocketService websocketService,
                               OpcUaDashboardService opcUaDashboardService) {
        this.websocketService = websocketService;
        this.opcUaDashboardService = opcUaDashboardService;
    }
    @MessageMapping("sensor/data/onload")
    public void sendBatch() {
        opcUaDashboardService.getCurrentNodeValueMap().forEach((opcuaNodes, currentValue) -> {
            websocketService.send(opcuaNodes.getName(),currentValue);
        });

    }
}
