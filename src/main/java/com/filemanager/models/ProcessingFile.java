package com.filemanager.models;

import java.io.File;

import com.drew.metadata.Metadata;
import com.filemanager.models.enums.FileStatus;

public class ProcessingFile {

    private File file;
    final private File parentDir;
    final private String originalName;
    private FileStatus status = FileStatus.UNDEFINED;
    private String statusMessage;
    private Metadata metadata;

    public ProcessingFile(File file) {
        this.file = file;
        this.parentDir = file.getParentFile();
        this.originalName = file.getName();
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getParentDir() {
        return this.parentDir;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public FileStatus getStatus() {
        return this.status;
    }

    public void setStatus(FileStatus status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
