package beerbrewers.sensorreading;

import beerbrewers.batch.Batch;
import beerbrewers.operation.Operation;
import beerbrewers.sensor.Sensor;
import beerbrewers.worker.Worker;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "SensorReading")
@Table(name = "sensor_reading")
public class SensorReading {

    @Id
    @SequenceGenerator(
            name = "sensor_reading_sequence",
            sequenceName = "sensor_reading_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sensor_reading_sequence"
    )
    @Column(
            name = "sensor_reading_id",
            updatable = false
    )
    private Long sensorReadingId;


    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Operation.class,
            optional = false
    )
//    @JoinColumn(name = "operation_id")
    private Operation operation;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Sensor.class,
            optional = false
    )
    private Sensor sensor;

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

    protected SensorReading() {}

    public SensorReading(Operation operation, Sensor sensor, Timestamp timestamp, float sensorValue) {
        this.operation = operation;
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.sensorValue = sensorValue;
    }

    public SensorReading(Long sensorReadingId, Operation operation, Sensor sensor, Timestamp timestamp, float sensorValue) {
        this.sensorReadingId = sensorReadingId;
        this.operation = operation;
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.sensorValue = sensorValue;
    }

    /**
     * Getters, setters and toString
     * @return
     */

    public Long getSensorReadingId() {
        return sensorReadingId;
    }

    public void setSensorReadingId(Long sensorReadingId) {
        this.sensorReadingId = sensorReadingId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
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

    @Override
    public String toString() {
        return "SensorReading{" +
                "sensorReadingId=" + sensorReadingId +
                ", operation=" + operation +
                ", sensor=" + sensor +
                ", timestamp=" + timestamp +
                ", sensorValue=" + sensorValue +
                '}';
    }
}
