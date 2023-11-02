package beerbrewers.brewmaster9000.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Batch {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long batchId;
    private long brewId;
    private long amount;
    private long speed;
    private boolean isCompleted;
    private long defectiveCount;

    public Batch(long brewId, long amount, long speed, boolean isCompleted, long defectiveCount) {
        super();
        this.brewId = brewId;
        this.amount = amount;
        this.speed = speed;
        this.isCompleted = isCompleted;
        this.defectiveCount = defectiveCount;
    }

    public long getBrewId() {
        return brewId;
    }

    public void setBrewId(long brewId) {
        this.brewId = brewId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getDefectiveCount() {
        return defectiveCount;
    }

    public void setDefectiveCount(long defectiveCount) {
        this.defectiveCount = defectiveCount;
    }
}
