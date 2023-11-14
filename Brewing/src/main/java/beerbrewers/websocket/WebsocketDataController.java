package beerbrewers.websocket;

import beerbrewers.operation.Operation;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketDataController {

    private WebsocketService websocketService;



    @Autowired
    @MessageMapping("/sensor/data")
    @SendTo("/sensor/data/")


}
