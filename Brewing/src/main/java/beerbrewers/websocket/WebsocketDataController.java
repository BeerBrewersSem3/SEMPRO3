package beerbrewers.websocket;

import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketDataController {
    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebsocketDataController(OpcUaNodeUpdateManager opcUaNodeUpdateManager, SimpMessagingTemplate messagingTemplate) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/subscribe")
    public void subscribeToNode(String nodeName) {
        OpcuaNodes opcuaNode = OpcuaNodes.valueOf(nodeName);
        opcUaNodeUpdateManager.addObserver(opcuaNode, (node, newState) -> {
            // Send the updated value to the WebSocket clients
            messagingTemplate.convertAndSend("/sensor/data/" + nodeName, newState);
        });
    }
}
