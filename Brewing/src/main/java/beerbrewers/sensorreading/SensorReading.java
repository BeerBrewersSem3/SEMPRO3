package beerbrewers.sensorreading;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class SensorReading {

    /**
     * Composite key created with a java class using the @Embeddable annotation. The composite key has operationId
     * and sensorId as attributes.
     */
    @EmbeddedId
    private SensorReadingId sensorReadingId;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private float sensorValue;

    protected SensorReading() {

    }

    public SensorReading(Timestamp timestamp, float sensorValue) {
        this.timestamp = timestamp;
        this.sensorValue = sensorValue;
    }

    public SensorReading(SensorReadingId sensorReadingId, Timestamp timestamp, float sensorValue) {
        this.sensorReadingId = sensorReadingId;
        this.timestamp = timestamp;
        this.sensorValue = sensorValue;
    }

    public SensorReadingId getSensorReadingId() {
        return sensorReadingId;
    }

    public void setSensorReadingId(SensorReadingId sensorReadingId) {
        this.sensorReadingId = sensorReadingId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(float sensorValue) {
        this.sensorValue = sensorValue;
    }
}
