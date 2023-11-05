package beerbrewers.operationtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/operationtype")
public class OperationTypeController {
    private final OperationTypeService operationTypeService;

    @Autowired
    private OperationTypeController(OperationTypeService operationTypeService) {
        this.operationTypeService = operationTypeService;
    }

    @GetMapping
    public List<OperationType> getOperationTypes() {
        return operationTypeService.getOperationTypes();
    }

}
