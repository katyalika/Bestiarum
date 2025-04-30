package com.mycompany.bestiarum.model.exporters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.util.List;

/**
 *
 * @author lihac
 */
public class JSONExporter implements MonsterExporter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void export(File file, List<Monster> monsters) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, monsters);
    }
}
