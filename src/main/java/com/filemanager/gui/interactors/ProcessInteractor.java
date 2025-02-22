package com.filemanager.gui.interactors;

import java.util.function.Consumer;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public final class ProcessInteractor {

    private final ProcessingTask task;
    private final Consumer<ProcessingTask> onStartProcess;
    private final ListProperty<ProcessingFile> allFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ProcessInteractor(ProcessingTask task, Consumer<ProcessingTask> onStartProcess) {
        this.task = task;
        this.onStartProcess = onStartProcess;

        this.allFiles.set(FXCollections.observableArrayList());
        task.getProcessibleFilesProperty().addListener((obs, oldList, newList) -> updateAllFiles());
        task.getUnprocessibleFilesProperty().addListener((obs, oldList, newList) -> updateAllFiles());
        this.updateAllFiles();
    }

    private void updateAllFiles() {
        this.allFiles.setAll(task.getProcessibleFilesProperty());
        this.allFiles.addAll(task.getUnprocessibleFilesProperty());
    }

    public String getTaskStrategy() {
        return this.task.getStrategy().getDisplayName();
    }

    public StringProperty getTaskStatusMessage() {
        return this.task.getStatusMessageProperty();
    }

    public String getFolderPath() {
        return this.task.getFolderPath();
    }

    public Integer getAllFilesSize() {
        return this.allFiles.size();
    }

    public Integer getProcesibledFilesSize() {
        return this.task.getProcessibleFiles().size();
    }

    public Integer getUnprocesibledFilesSize() {
        return this.task.getUnprocessibleFiles().size();
    }

    public ListProperty<ProcessingFile> getAllFiles() {
        return this.allFiles;
    }

    public void handleStartProcess() {
        this.onStartProcess.accept(this.task);
    }
}
