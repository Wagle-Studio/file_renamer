package com.filemanager.models;

import java.io.File;

public class ProcessingFile {

    private Boolean processable;
    private File file;

    public ProcessingFile(Boolean processable, File file) {
        this.processable = processable;
        this.file = file;
    }

    public Boolean isProcessable() {
        return processable;
    }

    public File getFile() {
        return file;
    }
}
