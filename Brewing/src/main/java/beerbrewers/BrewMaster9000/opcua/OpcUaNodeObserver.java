package beerbrewers.BrewMaster9000.opcua;

public interface OpcUaNodeObserver {
    void onNodeUpdate(OpcUaNodes node, String newState);
}

