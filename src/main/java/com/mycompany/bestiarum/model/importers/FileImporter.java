package com.mycompany.bestiarum.model.importers;

import com.mycompany.bestiarum.model.Monster;
import java.io.File;
import java.util.List;

/**
 *
 * @author lihac
 */
public interface FileImporter {
    void setNext(FileImporter next);
    List<Monster> importFile(File file) throws Exception;
    boolean canHandle(File file);
}
