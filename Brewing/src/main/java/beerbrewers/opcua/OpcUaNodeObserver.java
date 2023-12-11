package beerbrewers.opcua;

import java.util.List;

public interface OpcUaNodeObserver {

    void onNodeUpdate(OpcUaNode node, String newState);

    List<OpcUaNode> getSubscribedNodes();
}
