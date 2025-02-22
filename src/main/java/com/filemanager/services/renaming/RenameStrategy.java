package com.filemanager.services.renaming;

import java.util.List;

import com.filemanager.models.ProcessingFile;

public interface RenameStrategy {

    public String getDisplayName();

    default List<RenameStrategy> getStrategyPreprocess() {
        return List.of();
    }

    public Boolean validateFileRequirements(ProcessingFile file);

    public List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess);
}
