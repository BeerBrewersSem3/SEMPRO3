package beerbrewers.brew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/brew")
public class BrewController {
    private final BrewService brewService;

    @Autowired
    private BrewController(BrewService brewService) {
        this.brewService = brewService;
    }

    @GetMapping
    public List<Brew> getBrews() {
        return brewService.getBrews();
    }
}
