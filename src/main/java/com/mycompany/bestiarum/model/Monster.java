package com.mycompany.bestiarum.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author lihac
 */
public class Monster {

    private final UUID id = UUID.randomUUID();
    private String name;
    private String description;
    private int dangerLevel;
    private String source;
    private List<String> habitats = new ArrayList<>();
    private Date firstMentioned;
    private List<String> vulnerabilities = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();
    private List<String> immunities = new ArrayList<>();
    private String activity;
    private List<Map<String, Object>> poisonRecipe = new ArrayList<>();
    private List<Map<String, Object>> oilRecipe = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(int dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getHabitats() {
        return habitats;
    }

    public void setHabitats(List<String> habitats) {
        this.habitats = habitats;
    }

    public void addHabitat(String habitat) {
        this.habitats.add(habitat);
    }

    public Date getFirstMentioned() {
        return firstMentioned;
    }

    public void setFirstMentioned(Date firstMentioned) {
        this.firstMentioned = firstMentioned;
    }

    public void setFirstMentioned(String dateStr) throws ParseException {
        this.firstMentioned = dateFormat.parse(dateStr);
    }

    public String getFirstMentionedAsString() {
        return firstMentioned != null ? dateFormat.format(firstMentioned) : "";
    }

    public List<String> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<String> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public void addVulnerability(String vulnerability) {
        this.vulnerabilities.add(vulnerability);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public List<String> getImmunities() {
        return immunities;
    }

    public void setImmunities(List<String> immunities) {
        this.immunities = immunities;
    }

    public void addImmunity(String immunity) {
        this.immunities.add(immunity);
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<Map<String, Object>> getPoisonRecipe() {
        return poisonRecipe;
    }

    public void setPoisonRecipe(List<Map<String, Object>> poisonRecipe) {
        this.poisonRecipe = poisonRecipe;
    }

    public void addPoisonIngredient(String name, int quantity) {
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("name", name);
        ingredient.put("quantity", quantity);
        this.poisonRecipe.add(ingredient);
    }

    public List<Map<String, Object>> getOilRecipe() {
        return oilRecipe;
    }

    public void setOilRecipe(List<Map<String, Object>> oilRecipe) {
        this.oilRecipe = oilRecipe;
    }

    public void addOilIngredient(String name, int quantity) {
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("name", name);
        ingredient.put("quantity", quantity);
        this.oilRecipe.add(ingredient);
    }

    // Методы для установки параметров рецептов
    public void setPoisonRecipeParams(String prepTime, String effectiveness) {
        this.parameters.put("poison_prep_time", prepTime);
        this.parameters.put("poison_effectiveness", effectiveness);
    }

    public void setOilRecipeParams(String prepTime, String effectiveness) {
        this.parameters.put("oil_prep_time", prepTime);
        this.parameters.put("oil_effectiveness", effectiveness);
    }

    @Override
    public String toString() {
        return "Monster{"
                + "name='" + name + '\''
                + ", dangerLevel=" + dangerLevel
                + ", habitats=" + habitats
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Monster monster = (Monster) o;
        return id.equals(monster.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    
}
