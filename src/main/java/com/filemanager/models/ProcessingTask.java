package com.filemanager.models;

import java.util.List;

import com.filemanager.models.enums.FileStatus;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;

public class ProcessingTask {

    final private String folderPath;
    final private RenameStrategy strategy;
    private TaskStatus status = TaskStatus.PROCESSING;
    private String statusMessage;
    private List<ProcessingFile> processibleFiles;
    private List<ProcessingFile> unprocessibleFiles;

    public ProcessingTask(String folderPath, RenameStrategy strategy) {
        this.folderPath = folderPath;
        this.strategy = strategy;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public RenameStrategy getStrategy() {
        return this.strategy;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<ProcessingFile> getProcessibleFiles() {
        return this.processibleFiles;
    }

    public void setProcessibleFiles(List<ProcessingFile> processibleFiles) {
        this.processibleFiles = processibleFiles;
    }

    public List<ProcessingFile> getUnprocessibleFiles() {
        return this.unprocessibleFiles;
    }

    public void setUnprocessibleFiles(List<ProcessingFile> unprocessibleFiles) {
        this.unprocessibleFiles = unprocessibleFiles;
    }

    public void addUnprocessibleFiles(List<ProcessingFile> unprocessibleFiles) {
        unprocessibleFiles.forEach(file -> this.unprocessibleFiles.add(file));
    }

    public void results() {
        List<ProcessingFile> renamedFiles = processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.PROCESSED)
                .toList();

        List<ProcessingFile> failedFiles = processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.UNPROCESSABLE)
                .toList();

        System.out.println("\n📋 Dossier   : " + folderPath);
        System.out.println("📋 Stratégie : " + strategy.getClass().getSimpleName());
        System.out.println("📋 Status    : " + status + " - message : " + statusMessage);

        System.out.println("\n📁 Total fichiers        : " + renamedFiles.size());
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
