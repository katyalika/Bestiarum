package com.mycompany.bestiarum.model.exporters;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author lihac
 */
public class YAMLExporter implements MonsterExporter {

    private final Yaml yaml = new Yaml();

    @Override
    public void export(File file, List<Monster> monsters) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(monsters, writer);
        }
    }
}
