package beerbrewers.brewmaster9000.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class OperationType {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long operationTypeId;
    private String name;
    private List<Long> batchIds;

    public OperationType(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBatchIds() {
        return batchIds;
    }

    public void setBatchIds(List<Long> batchIds) {
        this.batchIds = batchIds;
    }
}
