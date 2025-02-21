package com.filemanager.models;

import java.util.List;

import com.filemanager.models.enums.FileStatus;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProcessingTask {

    final private StringProperty folderPath = new SimpleStringProperty();
    final private ObjectProperty<RenameStrategy> strategy = new SimpleObjectProperty<>();
    final private ObjectProperty<TaskStatus> status = new SimpleObjectProperty<>(TaskStatus.PROCESSING);
    final private StringProperty statusMessage = new SimpleStringProperty();
    final private ListProperty<ProcessingFile> processibleFiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    final private ListProperty<ProcessingFile> unprocessibleFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ProcessingTask(String folderPath, RenameStrategy strategy) {
        this.folderPath.set(folderPath);
        this.strategy.set(strategy);
    }

    public StringProperty getFolderPathProperty() {
        return this.folderPath;
    }

    public String getFolderPath() {
        return this.folderPath.get();
    }

    public ObjectProperty<RenameStrategy> getStrategyProperty() {
        return this.strategy;
    }

    public RenameStrategy getStrategy() {
        return this.strategy.get();
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

    public ListProperty<ProcessingFile> getProcessibleFilesProperty() {
        return this.processibleFiles;
    }

    public ObservableList<ProcessingFile> getProcessibleFiles() {
        return this.processibleFiles.get();
    }

    public void setProcessibleFiles(List<ProcessingFile> processibleFiles) {
        this.processibleFiles.set(FXCollections.observableArrayList(processibleFiles));
    }

    public ListProperty<ProcessingFile> getUnprocessibleFilesProperty() {
        return this.unprocessibleFiles;
    }

    public ObservableList<ProcessingFile> getUnprocessibleFiles() {
        return this.unprocessibleFiles.get();
    }

    public void setUnprocessibleFiles(List<ProcessingFile> unprocessibleFiles) {
        this.unprocessibleFiles.set(FXCollections.observableArrayList(unprocessibleFiles));
    }

    public void addUnprocessibleFiles(List<ProcessingFile> unprocessibleFiles) {
        this.unprocessibleFiles.addAll(unprocessibleFiles);
    }

    public void results() {
        List<ProcessingFile> renamedFiles = processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.PROCESSED)
                .toList();

        List<ProcessingFile> failedFiles = FXCollections.observableArrayList();
        failedFiles.addAll(processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.UNPROCESSABLE)
                .toList());
        failedFiles.addAll(unprocessibleFiles);

        System.out.println("\n📋 Dossier   : " + folderPath.get());
        System.out.println("📋 Stratégie : " + strategy.get().getClass().getSimpleName());
        System.out.println("📋 Status    : " + status.get() + " - message : " + statusMessage.get());

        System.out.println("\n📁 Total fichiers        : " + (renamedFiles.size() + failedFiles.size()));
        System.out.println("📄 Fichiers renommés     : " + renamedFiles.size());
        System.out.println("📄 Fichiers non renommés : " + failedFiles.size());

        System.out.println("\n✅ Fichiers renommés : " + (renamedFiles.isEmpty() ? "Aucun fichier renommé." : renamedFiles.size()));
        if (!renamedFiles.isEmpty()) {
            renamedFiles.forEach(file
                    -> System.out.println(" - " + file.getOriginalName() + " → " + file.getFile().getName())
            );
        }

        System.out.println("\n❌ Fichiers non renommés : " + (failedFiles.isEmpty() ? "Aucun échec." : failedFiles.size()) + "\n");
        if (!failedFiles.isEmpty()) {
            failedFiles.forEach(file
                    -> System.out.println(" - " + file.getOriginalName() + " ⚠ " + file.getStatusMessage())
            );
        }
    }
}
