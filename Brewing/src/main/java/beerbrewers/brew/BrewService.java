package beerbrewers.brew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrewService {
    private final BrewRepository brewRepository;

    @Autowired
    public BrewService(BrewRepository brewRepository) {
        this.brewRepository = brewRepository;
    }

    public List<Brew> getBrews() {
        return brewRepository.findAll();
    }
}
