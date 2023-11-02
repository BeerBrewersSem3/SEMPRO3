package beerbrewers.brewmaster9000.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long sensorId;
    private String name;
    private long opcNamespaceIndex;
    private String opcIdentifier;

    public Sensor(String name, long opcNamespaceIndex, String opcIdentifier) {
        super();
        this.name = name;
        this.opcNamespaceIndex = opcNamespaceIndex;
        this.opcIdentifier = opcIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOpcNamespaceIndex() {
        return opcNamespaceIndex;
    }

    public void setOpcNamespaceIndex(long opcNamespaceIndex) {
        this.opcNamespaceIndex = opcNamespaceIndex;
    }

    public String getOpcIdentifier() {
        return opcIdentifier;
    }

    public void setOpcIdentifier(String opcIdentifier) {
        this.opcIdentifier = opcIdentifier;
    }
}
