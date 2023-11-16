package beerbrewers.operationinputs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationInputsService {
    private final OperationInputsRepository operationInputsRepository;

    @Autowired
    public OperationInputsService(OperationInputsRepository operationInputsRepository) {
        this.operationInputsRepository = operationInputsRepository;
    }

    public List<OperationInputs> getOperationInputs() {
        return operationInputsRepository.findAll();
    }
}
