package beerbrewers.opcua;

public interface OpcUaNodeObserver {

    void onNodeUpdate(OpcuaNodes node, String newState);
}
