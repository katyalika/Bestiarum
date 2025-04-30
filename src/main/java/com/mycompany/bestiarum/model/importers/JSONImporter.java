package com.mycompany.bestiarum.model.importers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author lihac
 */
public class JSONImporter implements FileImporter {

    private FileImporter next;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void setNext(FileImporter next) {
        this.next = next;
    }

    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }

    @Override
    public List<Monster> importFile(File file) throws Exception {
        if (!canHandle(file)) {
            if (next != null) {
                return next.importFile(file);
            }
            throw new UnsupportedOperationException("Unsupported file format: " + file.getName());
        }

        Map<String, Object> jsonMap = mapper.readValue(file, Map.class);
        List<Map<String, Object>> creatures = (List<Map<String, Object>>) jsonMap.get("creatures");
        List<Monster> monsters = new ArrayList<>();

        for (Map<String, Object> creature : creatures) {
            monsters.add(parseMonster(creature));
        }

        return monsters;
    }

    private Monster parseMonster(Map<String, Object> data) throws ParseException {
        Monster monster = new Monster();

        monster.setName((String) data.get("name"));
        monster.setDescription((String) data.get("description"));
        monster.setDangerLevel(((Number) data.get("danger_level")).intValue());

        Map<String, Object> habitat = (Map<String, Object>) data.get("habitat");
        if (habitat.get("region") instanceof List) {
            monster.setHabitats((List<String>) habitat.get("region"));
        } else {
            List<String> regions = new ArrayList<>();
            regions.add((String) habitat.get("region"));
            monster.setHabitats(regions);
        }

        monster.setFirstMentioned((String) data.get("first_mentioned"));

        Map<String, Object> vulnerabilities = (Map<String, Object>) data.get("vulnerabilities");
        if (vulnerabilities.get("vulnerability") instanceof List) {
            monster.setVulnerabilities((List<String>) vulnerabilities.get("vulnerability"));
        } else {
            List<String> vulns = new ArrayList<>();
            vulns.add((String) vulnerabilities.get("vulnerability"));
            monster.setVulnerabilities(vulns);
        }

        monster.getParameters().putAll((Map<String, String>) data.get("parameters"));

        Map<String, Object> immunities = (Map<String, Object>) data.get("immunities");
        if (immunities.get("immunity") instanceof List) {
            monster.setImmunities((List<String>) immunities.get("immunity"));
        } else {
            List<String> imms = new ArrayList<>();
            imms.add((String) immunities.get("immunity"));
            monster.setImmunities(imms);
        }

        monster.setActivity((String) data.get("activity"));

        if (data.containsKey("poison_recipe")) {
            Map<String, Object> poisonRecipe = (Map<String, Object>) data.get("poison_recipe");
            monster.getPoisonRecipe().addAll(parseRecipe(poisonRecipe));
            monster.setPoisonRecipeParams(
                    (String) poisonRecipe.get("prep_time"),
                    (String) poisonRecipe.get("effectiveness")
            );
        }

        if (data.containsKey("oil_recipe")) {
            Map<String, Object> oilRecipe = (Map<String, Object>) data.get("oil_recipe");
            monster.getOilRecipe().addAll(parseRecipe(oilRecipe));
            monster.setOilRecipeParams(
                    (String) oilRecipe.get("prep_time"),
                    (String) oilRecipe.get("effectiveness")
            );
        }

        return monster;
    }

    private List<Map<String, Object>> parseRecipe(Map<String, Object> recipe) {
        List<Map<String, Object>> ingredients = new ArrayList<>();
        List<Map<String, Object>> recipeItems = (List<Map<String, Object>>) recipe.get("ingredient");

        for (Map<String, Object> item : recipeItems) {
            Map<String, Object> ingredient = new HashMap<>();
            ingredient.put("name", item.get("#text"));
            ingredient.put("quantity", Integer.parseInt(item.get("@quantity").toString()));
            ingredients.add(ingredient);
        }

        return ingredients;
    }
}