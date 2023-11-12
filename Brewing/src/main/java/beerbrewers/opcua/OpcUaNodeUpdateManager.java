package beerbrewers.opcua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OpcUaNodeUpdateManager {

    private static final Logger logger = LoggerFactory.getLogger(OpcUaNodeUpdateManager.class);

    private Map<OpcuaNodes, List<OpcUaNodeObserver>> observers = new ConcurrentHashMap<>();

    public void addObserver(OpcuaNodes node, OpcUaNodeObserver observer) {
        observers.computeIfAbsent(node, k -> new ArrayList<>()).add(observer);
    }


    public void notifyObservers(OpcuaNodes node, String newState) {
        List<OpcUaNodeObserver> nodeObservers = observers.getOrDefault(node, Collections.emptyList());
        for (OpcUaNodeObserver observer : nodeObservers) {
            observer.onNodeUpdate(node, newState);
            logger.debug("OpcUaNodeUpdateManager notified observer: " + observer.getClass().getSimpleName() + " of node: " + node.getName() + " with new state: " + newState);
        }
    }
}
