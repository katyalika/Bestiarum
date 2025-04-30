package com.mycompany.bestiarum.model.importers;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author lihac
 */
public class YAMLImporter implements FileImporter {

    private FileImporter next;

    @Override
    public void setNext(FileImporter next) {
        this.next = next;
    }

    @Override
    public boolean canHandle(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yaml") || name.endsWith(".yml");
    }

    @Override
    public List<Monster> importFile(File file) throws Exception {
        if (!canHandle(file)) {
            if (next != null) {
                return next.importFile(file);
            }
            throw new UnsupportedOperationException("Unsupported file format: " + file.getName());
        }

        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(options);

        try (InputStream inputStream = new FileInputStream(file)) {
            Map<String, Object> yamlData = yaml.load(inputStream);
            List<Map<String, Object>> creatures = (List<Map<String, Object>>) yamlData.get("creatures");
            List<Monster> monsters = new ArrayList<>();

            for (Map<String, Object> creature : creatures) {
                monsters.add(parseMonster(creature));
            }

            return monsters;
        }
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
            ingredient.put("name", item.get("text"));
            ingredient.put("quantity", ((Number) item.get("quantity")).intValue());
            ingredients.add(ingredient);
        }

        return ingredients;
    }
}