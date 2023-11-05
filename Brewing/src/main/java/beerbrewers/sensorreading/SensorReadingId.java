package beerbrewers.sensorreading;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class SensorReadingId implements Serializable {
    private long operationId;
    private long sensorId;

    public SensorReadingId(long operationId, long sensorId) {
        this.operationId = operationId;
        this.sensorId = sensorId;
    }

    public long getOperationId() {
        return operationId;
    }

    public long getSensorId() {
        return sensorId;
    }

}
