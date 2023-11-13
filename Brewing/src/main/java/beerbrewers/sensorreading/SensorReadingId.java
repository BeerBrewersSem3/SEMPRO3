package beerbrewers.sensorreading;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SensorReadingId implements Serializable {
    private Long operationId;
    private Long sensorId;

    public SensorReadingId(Long operationId, Long sensorId) {
        this.operationId = operationId;
        this.sensorId = sensorId;
    }

    /**
     * In Hibernate, when using composite keys, it's a good practice to implement the equals and hashCode methods
     * to ensure proper equality comparison.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorReadingId that = (SensorReadingId) o;
        return Objects.equals(operationId, that.operationId) && Objects.equals(sensorId, that.sensorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, sensorId);
    }

    public Long getOperationId() {
        return operationId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    @Override
    public String toString() {
        return "SensorReadingId{" +
                "operationId=" + operationId +
                ", sensorId=" + sensorId +
                '}';
    }
}
