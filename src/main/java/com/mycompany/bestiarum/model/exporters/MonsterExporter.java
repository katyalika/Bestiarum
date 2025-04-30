package com.mycompany.bestiarum.model.exporters;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.util.List;

/**
 *
 * @author lihac
 */
public interface MonsterExporter {
    void export(File file, List<Monster> monsters) throws Exception;
}
 