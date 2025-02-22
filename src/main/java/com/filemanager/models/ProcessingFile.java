package com.filemanager.models;

import java.io.File;

import com.filemanager.models.enums.FileStatus;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProcessingFile {

    final private ObjectProperty<File> file = new SimpleObjectProperty<>();
    final private ObjectProperty<File> parentDir = new SimpleObjectProperty<>();
    final private StringProperty originalName = new SimpleStringProperty();
    final private StringProperty currentName = new SimpleStringProperty();
    final private ObjectProperty<FileStatus> status = new SimpleObjectProperty<>(FileStatus.UNDEFINED);
    final private StringProperty statusMessage = new SimpleStringProperty();

    public ProcessingFile(File file) {
        this.file.set(file);
        this.parentDir.set(file.getParentFile());
        this.originalName.set(file.getName());
    }

    public ObjectProperty<File> getFileProperty() {
        return this.file;
    }

    public File getFile() {
        return this.file.get();
    }

    public void setFile(File file) {
        this.file.set(file);
        this.currentName.set(file.getName());
    }

    public ObjectProperty<File> getParentDirProperty() {
        return this.parentDir;
    }

    public File getParentDir() {
        return this.parentDir.get();
    }

    public StringProperty getOriginalNameProperty() {
        return this.originalName;
    }

    public String getOriginalName() {
        return this.originalName.get();
    }

    public StringProperty getCurrentNameProperty() {
        return this.currentName;
    }

    public String getCurrentName() {
        return this.currentName.get();
    }

    public ObjectProperty<FileStatus> getStatusProperty() {
        return this.status;
    }

    public FileStatus getStatus() {
        return this.status.get();
    }

    public void setStatus(FileStatus status, String message) {
        this.status.set(status);
        this.statusMessage.set(message);
    }

    public StringProperty getStatusMessageProperty() {
        return this.statusMessage;
    }

    public String getStatusMessage() {
        return this.statusMessage.get();
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage.set(statusMessage);
    }
}
