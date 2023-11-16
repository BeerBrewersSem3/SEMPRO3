package beerbrewers.machine;

import beerbrewers.batch.Batch;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class MachineController {
    private Batch batch;
    private MachineService machineService;
    @MessageMapping("batch/newBatch")
    private void sendBatch(Map<String, String> batchMap) {

        int bitchMap = Integer.parseInt(batchMap.get("batchID"));
        System.out.println(bitchMap);
    }



}
