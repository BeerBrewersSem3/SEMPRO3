package beerbrewers.websocket;

import beerbrewers.opcua.OpcUaDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public WebsocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void send(String name, String state) {
        simpMessagingTemplate.convertAndSend("/sensor/data/" + name, state);
        logger.debug("Websocket message sent for node {}: {}", name, state);
    }
}
