package beerbrewers.operationtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationTypeService {
    private final OperationTypeRepository operationTypeRepository;

    @Autowired
    public OperationTypeService(OperationTypeRepository operationTypeRepository) {
        this.operationTypeRepository = operationTypeRepository;
    }

    public List<OperationType> getOperationTypes() {
        return operationTypeRepository.findAll();
    }

}
