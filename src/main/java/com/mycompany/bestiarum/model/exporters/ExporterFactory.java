package com.mycompany.bestiarum.model.exporters;

import java.io.File;

/**
 *
 * @author lihac
 */
public class ExporterFactory {

    public static MonsterExporter getExporter(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            return new JSONExporter();
        } else if (fileName.endsWith(".xml")) {
            return new XMLExporter();
        } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            return new YAMLExporter();
        }

        throw new IllegalArgumentException("Unsupported file format for file: " + file.getName());
    }

    public static MonsterExporter getExporter(String format) {
        switch (format.toLowerCase()) {
            case "json":
                return new JSONExporter();
            case "xml":
                return new XMLExporter();
            case "yaml":
            case "yml":
                return new YAMLExporter();
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}