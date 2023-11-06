package beerbrewers.BrewMaster9000.opcua;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OpcUaNodeUpdateManager {


    private Map<OpcUaNodes, List<OpcUaNodeObserver>> observers = new ConcurrentHashMap<>();

    public void addObserver(OpcUaNodes node, OpcUaNodeObserver observer) {
        observers.computeIfAbsent(node, k -> new ArrayList<>()).add(observer);
    }

    public void addObservers(List<OpcUaNodes> nodes, OpcUaNodeObserver observer) {
        for(OpcUaNodes node : nodes) {
            addObserver(node, observer);
        }
    }

    public void notifyObservers(OpcUaNodes node, String newState) {
        List<OpcUaNodeObserver> nodeObservers = observers.getOrDefault(node, Collections.emptyList());
        for (OpcUaNodeObserver observer : nodeObservers) {
            observer.onNodeUpdate(node, newState);
        }
    }
}