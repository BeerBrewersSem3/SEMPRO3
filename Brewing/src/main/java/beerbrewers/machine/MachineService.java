package beerbrewers.machine;

import beerbrewers.batch.BrewEnum;
import beerbrewers.opcua.OpcUaNodeUpdateManager;
import beerbrewers.opcua.OpcuaNodes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineService {

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;

    @Autowired
    public MachineService(OpcUaNodeUpdateManager opcUaNodeUpdateManager) {
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
    }

    @PostConstruct
    public void initializeSubscription(){
        opcUaNodeUpdateManager.addObserver(OpcuaNodes.CMD_CHANGE_REQUEST,this);
    }
    public boolean startBatch(int id, BrewEnum brewEnum, int amountOfBottles, int machineSpeed){
        return true;
    }
}
