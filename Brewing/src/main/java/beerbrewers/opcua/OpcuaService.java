package beerbrewers.opcua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcuaService {

    private OpcuaSubscriber subscriber;
    private OpcuaCommander commander;
    @Autowired
    public OpcuaService(OpcuaSubscriber subscriber, OpcuaCommander commander){
        this.subscriber = subscriber;
        this.commander = commander;
    }
}
