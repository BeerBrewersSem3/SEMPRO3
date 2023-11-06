package beerbrewers.brewing.websocket;

import beerbrewers.brewing.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void updateNodeState(String nodeName, String state) {
        template.convertAndSend("/sensor/data/" + nodeName, state);
    }

}