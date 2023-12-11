package beerbrewers.recipe;

import beerbrewers.batch.BatchService;
import beerbrewers.batch.Brew;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {

    private final BatchService batchService;

    public RecipeController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/brewTypes")
    public List<Map<String, Object>> getBatches() {
        // Convert BrewEnum values to a list of Maps
        List<Map<String, Object>> brewTypes = Arrays.stream(Brew.values())
                .map(brewEnum -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("brewId", brewEnum.getBrewId());
                    map.put("barley", brewEnum.getBarley());
                    map.put("hops", brewEnum.getHops());
                    map.put("wheat", brewEnum.getWheat());
                    map.put("malt", brewEnum.getMalt());
                    map.put("yeast", brewEnum.getYeast());
                    map.put("name", brewEnum.getBrewName());
                    map.put("minMachSpeed",brewEnum.getMinMachSpeed());
                    map.put("maxMachSpeed",brewEnum.getMaxMachSpeed());
                    return map;
                })
                .collect(Collectors.toList());

        return brewTypes;
    }

    @GetMapping("/nextBatchId")
    public long getNextBatchId(){
        return batchService.getNextBatchId();
    }
}
