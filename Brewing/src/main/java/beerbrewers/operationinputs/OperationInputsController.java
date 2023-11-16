package beerbrewers.operationinputs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/operationinputs")
public class OperationInputsController {
    private final OperationInputsService operationInputsService;

    @Autowired
    private OperationInputsController(OperationInputsService operationInputsService) {
        this.operationInputsService = operationInputsService;
    }

    @GetMapping
    public List<OperationInputs> getOperationInputs() {
        return operationInputsService.getOperationInputs();
    }
}
