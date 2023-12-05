package beerbrewers.notification;

import beerbrewers.opcua.OpcuaNodes;
import beerbrewers.websocket.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TemperatureService {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureService.class);
    private final WebsocketService websocketService;

    public TemperatureService(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }


    public void temperatureWarning(OpcuaNodes node, String newState) {
        int temp = Integer.parseInt(newState);

        if (temp < 40 && temp < 60) {

            //websocketService.sendNotification(node, newState);
        }

        if (temp > 60) {


        }
    }
}
