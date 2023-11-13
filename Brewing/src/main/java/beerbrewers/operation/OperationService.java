package beerbrewers.operation;

import beerbrewers.batch.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {
    private final OperationRepository operationRepository;

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

}
