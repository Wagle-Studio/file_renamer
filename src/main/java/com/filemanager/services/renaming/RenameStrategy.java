package com.filemanager.services.renaming;

import java.io.File;

import com.filemanager.models.ProcessingFile;

public interface RenameStrategy {

    public Boolean validateFile(File file);

    public String rename(ProcessingFile file);
}
