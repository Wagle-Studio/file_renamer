package com.filemanager.gui.interactors;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;
import com.filemanager.services.renaming.enums.FileExtension;
import com.filemanager.utils.FileUtils;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public final class ProcessInteractor {

    private final ProcessingTask task;
    private final Consumer<ProcessingTask> onStartProcess;
    private final Runnable onCancel;
    private final ListProperty<ProcessingFile> allFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ProcessInteractor(
            ProcessingTask task,
            Consumer<ProcessingTask> onStartProcess,
            Runnable onCancel
    ) {
        this.task = task;
        this.onStartProcess = onStartProcess;
        this.onCancel = onCancel;

        this.allFiles.set(FXCollections.observableArrayList());
        task.getProcessableFilesProperty().addListener((obs, oldList, newList) -> updateAllFiles());
        task.getUnprocessableFilesProperty().addListener((obs, oldList, newList) -> updateAllFiles());
        this.updateAllFiles();
    }

    private void updateAllFiles() {
        this.allFiles.setAll(task.getProcessableFilesProperty());
        this.allFiles.addAll(task.getUnprocessableFilesProperty());
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

    public ListProperty<ProcessingFile> getAllFiles() {
        return this.allFiles;
    }

    public Integer getAllFilesSize() {
        return this.allFiles.size();
    }

    public ListProperty<ProcessingFile> getProcessableFiles() {
        return this.task.getProcessableFilesProperty();
    }

    public Integer getProcessableFilesSize() {
        return this.task.getProcessableFiles().size();
    }

    public Integer getUnprocessableFilesSize() {
        return this.task.getUnprocessableFiles().size();
    }

    public Optional<FileExtension> getFileExtension(File file) {
        return FileUtils.getFileExtension(file);
    }

    public void handleStartProcess() {
        this.onStartProcess.accept(this.task);
    }

    public void handleCancel() {
        this.onCancel.run();
    }
}
