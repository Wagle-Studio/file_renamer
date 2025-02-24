package com.filemanager.models;

import java.util.List;

import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.enums.FileExtension;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProcessingTask {

    private final StringProperty folderPath = new SimpleStringProperty();
    private final ObjectProperty<RenameStrategy> strategy = new SimpleObjectProperty<>();
    private final ListProperty<FileExtension> extensions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<TaskStatus> status = new SimpleObjectProperty<>(TaskStatus.UNDEFINED);
    private final StringProperty statusMessage = new SimpleStringProperty();
    private final ListProperty<ProcessingFile> processableFiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<ProcessingFile> unprocessableFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    public StringProperty getFolderPathProperty() {
        return this.folderPath;
    }

    public String getFolderPath() {
        return this.folderPath.get();
    }

    public void setFolderPath(String folderPath) {
        this.folderPath.set(folderPath);
    }

    public ObjectProperty<RenameStrategy> getStrategyProperty() {
        return this.strategy;
    }

    public RenameStrategy getStrategy() {
        return this.strategy.get();
    }

    public void setStrategy(RenameStrategy strategy) {
        this.strategy.set(strategy);
    }

    public ListProperty<FileExtension> getExtensionsProperty() {
        return this.extensions;
    }

    public ObservableList getExtensions() {
        return this.extensions.get();
    }

    public void setExtensions(List<FileExtension> extensions) {
        this.extensions.setAll(extensions);
    }

    public ObjectProperty<TaskStatus> getStatusProperty() {
        return this.status;
    }

    public TaskStatus getStatus() {
        return this.status.get();
    }

    public void setStatus(TaskStatus status, String message) {
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

    public ListProperty<ProcessingFile> getProcessableFilesProperty() {
        return this.processableFiles;
    }

    public ObservableList<ProcessingFile> getProcessableFiles() {
        return this.processableFiles.get();
    }

    public void setProcessableFiles(List<ProcessingFile> processableFiles) {
        this.processableFiles.setAll(processableFiles);
    }

    public ListProperty<ProcessingFile> getUnprocessableFilesProperty() {
        return this.unprocessableFiles;
    }

    public ObservableList<ProcessingFile> getUnprocessableFiles() {
        return this.unprocessableFiles.get();
    }

    public void setUnprocessableFiles(List<ProcessingFile> unprocessableFiles) {
        this.unprocessableFiles.setAll(unprocessableFiles);
    }

    public void addUnprocessableFiles(List<ProcessingFile> unprocessableFiles) {
        this.unprocessableFiles.addAll(unprocessableFiles);
    }
}
