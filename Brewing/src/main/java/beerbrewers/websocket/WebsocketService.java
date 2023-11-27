package beerbrewers.websocket;

import beerbrewers.opcua.OpcuaNodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketService {
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public WebsocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void send(String name, String state) {
        simpMessagingTemplate.convertAndSend("/sensor/data/" + name, state);
    }

    public void sendNotification(OpcuaNodes node, String state){
        simpMessagingTemplate.convertAndSend("/sensor/notification/" + node.getName(), state);
    }
}
