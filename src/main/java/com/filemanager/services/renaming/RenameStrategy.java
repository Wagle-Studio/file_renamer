package com.filemanager.services.renaming;

import java.util.List;

import com.drew.metadata.Metadata;
import com.filemanager.models.ProcessingFile;

public interface RenameStrategy {

    public String getDisplayName();

    public Boolean validateFileMetadata(Metadata metadata);

    default List<RenameStrategy> getPreprocess() {
        return List.of();
    }

    public List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess);
}
