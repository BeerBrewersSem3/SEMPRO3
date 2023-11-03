package beerbrewers.sensor;

import jakarta.persistence.*;

@Entity
@Table
public class Sensor {

    @Id
    @SequenceGenerator(
            name = "sensor_sequence",
            sequenceName = "sensor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_sequence"
    )
    private long sensorId;
    private String name;
    private long opcNamespaceIndex;
    private String opcIdentifier;

    @Transient
    private float currentSensorValue;

    protected Sensor() {}

    public Sensor(String name, long opcNamespaceIndex, String opcIdentifier) {
        this.name = name;
        this.opcNamespaceIndex = opcNamespaceIndex;
        this.opcIdentifier = opcIdentifier;
    }

    public Sensor(long sensorId, String name, long opcNamespaceIndex, String opcIdentifier) {
        this.sensorId = sensorId;
        this.name = name;
        this.opcNamespaceIndex = opcNamespaceIndex;
        this.opcIdentifier = opcIdentifier;
    }

    public long getSensorId() {
        return sensorId;
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

    public float getCurrentSensorValue() {
        return currentSensorValue;
    }

    public void setCurrentSensorValue(float currentSensorValue) {
        this.currentSensorValue = currentSensorValue;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId=" + sensorId +
                ", name='" + name + '\'' +
                ", opcNamespaceIndex=" + opcNamespaceIndex +
                ", opcIdentifier='" + opcIdentifier + '\'' +
                '}';
    }
}
