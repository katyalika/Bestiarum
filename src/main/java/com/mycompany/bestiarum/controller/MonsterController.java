package com.mycompany.bestiarum.controller;
import com.mycompany.bestiarum.model.Monster;
import com.mycompany.bestiarum.model.MonsterStorage;
import com.mycompany.bestiarum.model.exporters.MonsterExporterManager;
import com.mycompany.bestiarum.model.importers.FileImporter;
import com.mycompany.bestiarum.model.importers.JSONImporter;
import com.mycompany.bestiarum.model.importers.XMLImporter;
import com.mycompany.bestiarum.model.importers.YAMLImporter;
import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;

/**
 *
 * @author lihac
 */
public class MonsterController {
    private final MonsterStorage storage = new MonsterStorage();
    private final FileImporter fileImporter = createImporterChain();
    private final MonsterExporterManager exportManager = new MonsterExporterManager(storage);
    private final Map<File, List<UUID>> fileMonsterMap = new HashMap<>();

    public void importFiles(JFrame parent, Consumer<List<Monster>> onSuccess) {
        JFileChooser fileChooser = createFileChooser();
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                importFile(file, parent, onSuccess);
            }
        }
    }

    private void importFile(File file, JFrame parent, Consumer<List<Monster>> onSuccess) {
        try {
            List<Monster> monsters = fileImporter.importFile(file);
            List<UUID> ids = new ArrayList<>();

            for (Monster monster : monsters) {
                monster.setSource(file.getName());
                storage.addMonster(monster);
                ids.add(monster.getId());
            }

            fileMonsterMap.put(file, ids);
            onSuccess.accept(monsters);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Failed to import " + file.getName() + ": " + e.getMessage(),
                    "Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void exportMonsters(JFrame parent, List<Monster> monsters) {
        exportManager.exportData(parent, monsters);
    }

    public boolean updateMonster(UUID id, Monster newData) {
        return storage.updateMonster(id, newData);
    }

    public Optional<Monster> getMonster(UUID id) {
        return storage.getMonsterById(id);
    }

    public List<Monster> getAllMonsters() {
        return storage.getMonsters();
    }

    public List<Monster> getMonstersBySource(String source) {
        return storage.getMonstersBySource(source);
    }

    private FileImporter createImporterChain() {
        JSONImporter json = new JSONImporter();
        XMLImporter xml = new XMLImporter();
        YAMLImporter yaml = new YAMLImporter();

        json.setNext(xml);
        xml.setNext(yaml);

        return json;
    }

    private JFileChooser createFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fc.setMultiSelectionEnabled(true);
        fc.setDialogTitle("Select Files to Import");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "JSON, XML, YAML Files", "json", "xml", "yaml", "yml"));
        return fc;
    }
}
