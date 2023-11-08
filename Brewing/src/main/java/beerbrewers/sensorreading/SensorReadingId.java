package beerbrewers.sensorreading;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class SensorReadingId implements Serializable {
    private Long operationId;
    private Long sensorId;

    public SensorReadingId(Long operationId, Long sensorId) {
        this.operationId = operationId;
        this.sensorId = sensorId;
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
