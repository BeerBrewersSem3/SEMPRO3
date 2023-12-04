package beerbrewers.recipe;

import beerbrewers.batch.Batch;
import beerbrewers.batch.BrewEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/recipe/brewTypes")
public class RecipeController {

    @GetMapping
    public List<Map<String, Object>> getBatches() {
        // Convert BrewEnum values to a list of Maps
        List<Map<String, Object>> brewTypes = Arrays.stream(BrewEnum.values())
                .map(brewEnum -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("brewId", brewEnum.getBrewId());
                    map.put("barley", brewEnum.getBarley());
                    map.put("hops", brewEnum.getHops());
                    map.put("wheat", brewEnum.getWheat());
                    map.put("malt", brewEnum.getMalt());
                    map.put("yeast", brewEnum.getYeast());
                    map.put("name", brewEnum.getBrewName());
                    return map;
                })
                .collect(Collectors.toList());

        return brewTypes;
    }
}
