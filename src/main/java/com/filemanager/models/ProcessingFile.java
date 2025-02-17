package com.filemanager.models;

import java.io.File;

import com.drew.metadata.Metadata;
import com.filemanager.models.enums.FileStatus;

public class ProcessingFile {

    final private File file;
    final private File parentDir;
    final private String originalName;
    private FileStatus status = FileStatus.UNDEFINED;
    private String statusMessage;
    private Metadata metadata;

    public ProcessingFile(File file, String message) {
        this.file = file;
        this.parentDir = file.getParentFile();
        this.originalName = file.getName();
        this.status = FileStatus.UNPROCESSABLE;
        this.statusMessage = message;
    }

    public ProcessingFile(File file, Metadata metadata) {
        this.file = file;
        this.parentDir = file.getParentFile();
        this.originalName = file.getName();
        this.status = FileStatus.PROCESSABLE;
        this.metadata = metadata;
    }

    public File getFile() {
        return this.file;
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

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public void setStatus(FileStatus status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }
}
