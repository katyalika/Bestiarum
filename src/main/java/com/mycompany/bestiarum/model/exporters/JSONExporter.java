package com.mycompany.bestiarum.model.exporters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lihac
 */
public class JSONExporter {
    private final ObjectMapper mapper;

    public JSONExporter() {
        this.mapper = new ObjectMapper();
    }

    public void export(List<Monster> monsters, File outputFile) throws IOException {
        List<Map<String, Object>> exportData = new ArrayList<>();
        for (Monster monster : monsters) {
            Map<String, Object> monsterData = new HashMap<>();
            monsterData.put("name", monster.getName());
            monsterData.put("description", monster.getDescription());
            monsterData.put("danger_level", monster.getDangerLevel());
            monsterData.put("source", monster.getSource());
            monsterData.put("habitats", monster.getHabitats());
            monsterData.put("first_mentioned", monster.getFirstMentionedAsString());
            monsterData.put("vulnerabilities", monster.getVulnerabilities());
            monsterData.put("immunities", monster.getImmunities());
            monsterData.put("activity", monster.getActivity());

            // Добавляем рецепт
            Map<String, Object> recipeData = new HashMap<>();
            recipeData.put("ingredients", monster.getRecipe());
            recipeData.put("prep_time", monster.getParameter("prep_time"));
            recipeData.put("effectiveness", monster.getParameter("effectiveness"));
            monsterData.put("recipe", recipeData);

            exportData.add(monsterData);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, exportData);
    }
}
