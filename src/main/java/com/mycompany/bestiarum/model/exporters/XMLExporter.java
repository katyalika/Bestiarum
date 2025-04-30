package com.mycompany.bestiarum.model.exporters;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author lihac
 */
public class XMLExporter implements MonsterExporter {

    @Override
    public void export(File file, List<Monster> monsters) throws Exception {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file));

        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("monsters");

            for (Monster monster : monsters) {
                writeMonster(writer, monster);
            }

            writer.writeEndElement(); // monsters
            writer.writeEndDocument();
        } finally {
            writer.close();
        }
    }

    private void writeMonster(XMLStreamWriter writer, Monster monster) throws XMLStreamException {
        writer.writeStartElement("monster");

        writeElement(writer, "name", monster.getName());
        writeElement(writer, "description", monster.getDescription());
        writeElement(writer, "dangerLevel", String.valueOf(monster.getDangerLevel()));
        writeElement(writer, "source", monster.getSource());
        writeElement(writer, "activity", monster.getActivity());

        // Habitats
        for (String habitat : monster.getHabitats()) {
            writeElement(writer, "habitat", habitat);
        }

        // First mentioned date
        if (monster.getFirstMentioned() != null) {
            writeElement(writer, "firstMentioned", new SimpleDateFormat("yyyy-MM-dd").format(monster.getFirstMentioned()));
        }

        // Vulnerabilities
        for (String vulnerability : monster.getVulnerabilities()) {
            writeElement(writer, "vulnerability", vulnerability);
        }

        // Immunities
        for (String immunity : monster.getImmunities()) {
            writeElement(writer, "immunity", immunity);
        }

        // Parameters
        for (Map.Entry<String, String> entry : monster.getParameters().entrySet()) {
            writer.writeStartElement("parameter");
            writer.writeAttribute("key", entry.getKey());
            writer.writeCharacters(entry.getValue());
            writer.writeEndElement();
        }

        // Poison recipe
        if (!monster.getPoisonRecipe().isEmpty()) {
            writer.writeStartElement("poisonRecipe");
            for (Map<String, Object> ingredient : monster.getPoisonRecipe()) {
                writeRecipeIngredient(writer, ingredient);
            }
            writer.writeEndElement();
        }

        // Oil recipe
        if (!monster.getOilRecipe().isEmpty()) {
            writer.writeStartElement("oilRecipe");
            for (Map<String, Object> ingredient : monster.getOilRecipe()) {
                writeRecipeIngredient(writer, ingredient);
            }
            writer.writeEndElement();
        }

        writer.writeEndElement(); // monster
    }

    private void writeRecipeIngredient(XMLStreamWriter writer, Map<String, Object> ingredient) throws XMLStreamException {
        writer.writeStartElement("ingredient");
        writeElement(writer, "name", (String) ingredient.get("name"));
        writeElement(writer, "quantity", String.valueOf(ingredient.get("quantity")));
        writer.writeEndElement();
    }

    private void writeElement(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        if (value != null) {
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
    }
}
