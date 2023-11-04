package beerbrewers.operation;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class OperationType {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long operationTypeId;
    @Column(nullable = false)
    private String name;
    private List<Long> batchIds;

    public OperationType() {

    }
    public OperationType(String name) {
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
