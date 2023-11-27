package beerbrewers.opcua;

import java.util.List;

public interface OpcUaNodeObserver {

    void onNodeUpdate(OpcuaNodes node, String newState);

    List<OpcuaNodes> getSubscribedNodes();
}
