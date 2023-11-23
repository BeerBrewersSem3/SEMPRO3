package beerbrewers.machine;

import beerbrewers.batch.Batch;
import beerbrewers.websocket.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class MachineController {
    private Batch batch;
    private MachineService machineService;
    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
    @MessageMapping("batch/newBatch")
    public void sendBatch(Map<String, String> batchMap) {
        // Testing retrieval of batch data
        int batchID = Integer.parseInt(batchMap.get("batchID"));
        String batchType =  batchMap.get("batchType");
        int batchAmount = Integer.parseInt(batchMap.get("batchAmount"));
        int batchSpeed = Integer.parseInt(batchMap.get("batchSpeed"));

        logger.debug("ID: " + batchID +
                "\n type: " + batchType +
                "\n amount: " + batchAmount +
                "\n speed: " + batchSpeed);
    }
}
