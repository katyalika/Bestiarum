/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bestiarum.model.exporters;

import com.mycompany.bestiarum.model.Monster;
import com.mycompany.bestiarum.model.MonsterStorage;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author lihac
 */
public class MonsterExporterManager {

    private final MonsterStorage storage;

    public MonsterExporterManager(MonsterStorage storage) {
        this.storage = storage;
    }

    public void exportData(JFrame parent, List<Monster> monsters) {
        if (monsters == null || monsters.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "No monsters selected for export",
                    "Export Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = createExportFileChooser();
        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = ensureCorrectExtension(fileChooser);
            performExport(parent, file, monsters);
        }
    }

    private JFileChooser createExportFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle("Export Monsters");
        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("JSON Files (*.json)", "json"));
        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("XML Files (*.xml)", "xml"));
        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("YAML Files (*.yaml, *.yml)", "yaml", "yml"));

        return fileChooser;
    }

    private File ensureCorrectExtension(JFileChooser fileChooser) {
        File file = fileChooser.getSelectedFile();
        FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
        String path = file.getAbsolutePath();

        for (String ext : filter.getExtensions()) {
            if (path.toLowerCase().endsWith("." + ext.toLowerCase())) {
                return file;
            }
        }

        return new File(path + "." + filter.getExtensions()[0]);
    }

    private void performExport(JFrame parent, File file, List<Monster> monsters) {
        try {
            MonsterExporter exporter = ExporterFactory.getExporter(file);
            exporter.export(file, monsters);

            JOptionPane.showMessageDialog(parent,
                    "Successfully exported " + monsters.size() + " monsters to:\n" + file.getAbsolutePath(),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Export failed:\n" + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void exportAllMonsters(JFrame parent) {
        exportData(parent, storage.getMonsters());
    }
}
