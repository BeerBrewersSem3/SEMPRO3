package beerbrewers.brewing.websocket;

import beerbrewers.BrewMaster9000.opcua.OpcUaClientCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class WebSocketCommandController {

    @Autowired
    private OpcUaClientCommand opcUaClientCommand;

    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/command/sendCommand")
    public void sendCommand(Map<String, String> payload) {
        try {
            int number = Integer.parseInt(payload.get("number"));
            if (number >= 0 && number <= 5) {
                opcUaClientCommand.sendCommand(number);
                webSocketService.updateNodeState("/command/response", "Number sent successfully");
            } else {
                webSocketService.updateNodeState("/command/response", "Number must be between 0 and 5");
            }
        } catch (Exception e) {
            webSocketService.updateNodeState("/command/response", "Error: " + e.getMessage());
        }
    }

    @MessageMapping("/command/executeCommand")
    public void executeCommand() {
        try {
            opcUaClientCommand.executeCommand(true);
            webSocketService.updateNodeState("/command/response", "Boolean value set successfully");
        } catch (Exception e) {
            webSocketService.updateNodeState("/command/response", "Error: " + e.getMessage());
        }
    }
}