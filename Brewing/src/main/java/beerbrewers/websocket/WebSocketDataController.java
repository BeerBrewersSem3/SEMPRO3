package beerbrewers.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketDataController {


    @MessageMapping("/state/{nodeName}")
    @SendTo("/sensor/data/{nodeName}")
    public String getState(@DestinationVariable String nodeName, String state) {
        return state;
    }

}