package beerbrewers.sensorreading;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "sensor_reading")
@Table
public class SensorReading {

    /**
     * Composite key created with a java class using the @Embeddable annotation. The composite key has operationId
     * and sensorId as attributes.
     */
    @EmbeddedId
    @Column(
            name = "sensor_reading_id",
            updatable = false
    )
    private SensorReadingId sensorReadingId;

    @Column(
            name = "timestamp",
            nullable = false
    )
    private Timestamp timestamp;

    @Column(
            name = "sensor_value",
            nullable = false
    )
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
