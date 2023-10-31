package beerbrewers.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketStateController {

    @MessageMapping("/currentState")
    @SendTo("/sensor/data/currentState")
    public String currentState(String state) {
        return state;
    }
}