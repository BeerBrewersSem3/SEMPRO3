package beerbrewers.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/operation")
public class OperationController {
    private final OperationService operationService;

    @Autowired
    private OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public List<Operation> getOperations() {
        return operationService.getOperations();
    }

    @PostMapping
    public void saveOperation(@RequestBody Operation operation) {
        operationService.addNewOperation(operation);
    }
    
}
