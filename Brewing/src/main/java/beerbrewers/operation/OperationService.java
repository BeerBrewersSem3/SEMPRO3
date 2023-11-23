package beerbrewers.operation;

import beerbrewers.batch.Batch;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {
    private final OperationRepository operationRepository;

    private Operation currentRunningOperation = null;

    @Autowired
    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> getOperations() {
        return operationRepository.findAll();
    }

    public void addNewOperation(Operation operation) {
        operationRepository.save(operation);
    }

    public Operation getCurrentRunningOperation() {
        return currentRunningOperation;
    }

    public void setCurrentRunningOperation(Operation currentRunningOperation) {
        this.currentRunningOperation = currentRunningOperation;
    }
}
