package com.filemanager.services.renaming;

import java.util.List;

import com.filemanager.models.ProcessingFile;

public interface RenameStrategy {

    String getDisplayName();

    default List<RenameStrategy> getStrategyPreprocess() {
        return List.of();
    }

    Boolean validateFileRequirements(ProcessingFile file);

    List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess);
}
