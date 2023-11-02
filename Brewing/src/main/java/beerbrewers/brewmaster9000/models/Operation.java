package beerbrewers.brewmaster9000.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="operations")
public class Operation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long operationId;

    private long workerId;
    private long operationTypeId;
    private Date date;

    public Operation(long workerId, long operationTypeId, Date date) {
        super();
        this.workerId = workerId;
        this.operationTypeId = operationTypeId;
        this.date = date;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
