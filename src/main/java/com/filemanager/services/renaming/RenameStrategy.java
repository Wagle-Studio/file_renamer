package com.filemanager.services.renaming;

import java.util.List;

import com.drew.metadata.Metadata;
import com.filemanager.models.ProcessingFile;

public interface RenameStrategy {

    public Boolean validateFileMetadata(Metadata metadata);

    public void execute(List<ProcessingFile> files);
}
