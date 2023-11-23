package beerbrewers.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public void saveOperation(Map<String, String> operationMap) {
//        System.out.println("Operation: " + operation);
        System.out.println(operationMap.get("worker"));
        System.out.println(operationMap.get("operation"));
        System.out.println(operationMap);
//        operationService.addNewOperation(operation);
    }

}
