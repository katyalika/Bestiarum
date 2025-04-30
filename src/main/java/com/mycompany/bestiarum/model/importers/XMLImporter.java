package com.mycompany.bestiarum.model.importers;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author lihac
 */
public class XMLImporter implements FileImporter {

    private FileImporter next;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void setNext(FileImporter next) {
        this.next = next;
    }

    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }

    @Override
    public List<Monster> importFile(File file) throws Exception {
        if (!canHandle(file)) {
            if (next != null) {
                return next.importFile(file);
            }
            throw new UnsupportedOperationException("Unsupported file format: " + file.getName());
        }

        List<Monster> monsters = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(file));

        Monster currentMonster = null;
        String currentElement = null;
        Map<String, Object> currentRecipeIngredient = null;
        boolean inPoisonRecipe = false;
        boolean inOilRecipe = false;
        List<Map<String, Object>> currentRecipe = null;

        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    currentElement = reader.getLocalName();

                    switch (currentElement) {
                        case "creature":
                            currentMonster = new Monster();
                            break;
                        case "poison_recipe":
                            inPoisonRecipe = true;
                            currentRecipe = new ArrayList<>();
                            break;
                        case "oil_recipe":
                            inOilRecipe = true;
                            currentRecipe = new ArrayList<>();
                            break;
                        case "ingredient":
                            currentRecipeIngredient = new HashMap<>();
                            String quantity = reader.getAttributeValue(null, "quantity");
                            if (quantity != null) {
                                currentRecipeIngredient.put("quantity", quantity);
                            }
                            break;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (currentElement != null && currentRecipeIngredient != null && "ingredient".equals(currentElement)) {
                        currentRecipeIngredient.put("text", reader.getText().trim());
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String elementName = reader.getLocalName();

                    if (currentMonster != null) {
                        if (inPoisonRecipe || inOilRecipe) {
                            switch (elementName) {
                                case "ingredient":
                                    if (currentRecipeIngredient != null) {
                                        currentRecipe.add(currentRecipeIngredient);
                                        currentRecipeIngredient = null;
                                    }
                                    break;
                                case "prep_time":
                                case "effectiveness":
                                    currentMonster.getParameters().put(
                                            (inPoisonRecipe ? "poison_" : "oil_") + elementName,
                                            reader.getElementText().trim()
                                    );
                                    break;
                                case "poison_recipe":
                                    currentMonster.getPoisonRecipe().addAll(currentRecipe);
                                    inPoisonRecipe = false;
                                    currentRecipe = null;
                                    break;
                                case "oil_recipe":
                                    currentMonster.getOilRecipe().addAll(currentRecipe);
                                    inOilRecipe = false;
                                    currentRecipe = null;
                                    break;
                            }
                        } else {
                            String text = reader.getElementText().trim();
                            if (!text.isEmpty()) {
                                switch (elementName) {
                                    case "name":
                                        currentMonster.setName(text);
                                        break;
                                    case "description":
                                        currentMonster.setDescription(text);
                                        break;
                                    case "danger_level":
                                        currentMonster.setDangerLevel(Integer.parseInt(text));
                                        break;
                                    case "region":
                                        currentMonster.addHabitat(text);
                                        break;
                                    case "first_mentioned":
                                        currentMonster.setFirstMentioned(text);
                                        break;
                                    case "vulnerability":
                                        currentMonster.addVulnerability(text);
                                        break;
                                    case "height":
                                    case "weight":
                                        currentMonster.getParameters().put(elementName, text);
                                        break;
                                    case "immunity":
                                        currentMonster.addImmunity(text);
                                        break;
                                    case "activity":
                                        currentMonster.setActivity(text);
                                        break;
                                    case "creature":
                                        monsters.add(currentMonster);
                                        currentMonster = null;
                                        break;
                                }
                            }
                        }
                    }
                    currentElement = null;
                    break;
            }
        }

        reader.close();
        return monsters;
    }
}
