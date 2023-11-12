package beerbrewers.opcua;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcUaDashboardService implements OpcUaNodeObserver{

    private final OpcUaNodeUpdateManager opcUaNodeUpdateManager;
    private static final Logger logger = LoggerFactory.getLogger(OpcUaDashboardService.class);

    @Autowired
    public OpcUaDashboardService(OpcUaNodeUpdateManager opcUaNodeUpdateManager){
        this.opcUaNodeUpdateManager = opcUaNodeUpdateManager;
    }

    @PostConstruct
    public void initialize(){
        List<OpcuaNodes> nodesToSubscribe = List.of(
                OpcuaNodes.STATE_CURRENT,
                OpcuaNodes.CMD_CHANGE_REQUEST
        );
        nodesToSubscribe.forEach(node -> {
            opcUaNodeUpdateManager.addObserver(node, this);
            logger.info("OpcuaDashboardService subscribed to node: " + node.getName());
        });
    }
    @Override
    public void onNodeUpdate(OpcuaNodes node, String newState) {
        logger.info(node.getName() + " has new value of: " + newState);
    }
}
