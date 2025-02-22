package com.filemanager.gui.interactors;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ProcessInteractor {

    private final ProcessingTask task;
    private final Consumer<ProcessingTask> onStartProcess;

    public ProcessInteractor(ProcessingTask task, Consumer<ProcessingTask> onStartProcess) {
        this.task = task;
        this.onStartProcess = onStartProcess;
    }

    public String getStrategy() {
        return this.task.getStrategy().getClass().getSimpleName();
    }

    public String getFolderPath() {
        return this.task.getFolderPath();
    }

    public ObservableList<ProcessingFile> getProcessedFiles() {
        List<ProcessingFile> files = Stream.concat(this.task.getProcessibleFiles().stream(), this.task.getUnprocessibleFiles().stream()).toList();
        return FXCollections.observableArrayList(files);
    }

    public void handleStartProcess() {
        this.onStartProcess.accept(this.task);
    }
}
