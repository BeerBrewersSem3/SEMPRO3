package beerbrewers.operation;

import beerbrewers.worker.Worker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/operation")
public class OperationController {
    private final OperationService operationService;
    private final ObjectMapper objectMapper;
    @Autowired
    private OperationController(OperationService operationService, ObjectMapper objectMapper) {
        this.operationService = operationService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Operation> getOperations() {
        return operationService.getOperations();
    }

    @PostMapping
    public void saveOperation(@RequestBody String JSONstring) {
        try {
            // Convert JSON string to Operation object
            Operation operation = objectMapper.readValue(JSONstring, Operation.class);
            System.out.println(operation);
            operationService.addNewOperation(operation);
        } catch (IOException e) {
            // Handle JSON parsing exception
            e.printStackTrace();
        }
    }

}
