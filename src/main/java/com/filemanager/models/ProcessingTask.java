package com.filemanager.models;

import java.io.File;
import java.util.List;

import com.filemanager.models.enums.FileStatus;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;

public class ProcessingTask {

    final private String folderPath;
    final private RenameStrategy strategy;
    private File[] files;
    private TaskStatus status = TaskStatus.PROCESSING;
    private String statusMessage;
    private List<ProcessingFile> processibleFiles;
    private List<ProcessingFile> unprocessibleFiles;

    public ProcessingTask(String folderPath, RenameStrategy strategy, String message) {
        this.folderPath = folderPath;
        this.strategy = strategy;
        this.status = TaskStatus.ERROR;
        this.statusMessage = message;
    }

    public ProcessingTask(
            String folderPath,
            RenameStrategy strategy,
            File[] files,
            List<ProcessingFile> processibleFiles,
            List<ProcessingFile> unprocessibleFiles
    ) {
        this.folderPath = folderPath;
        this.strategy = strategy;
        this.files = files;
        this.processibleFiles = List.copyOf(processibleFiles);
        this.unprocessibleFiles = List.copyOf(unprocessibleFiles);
    }

    public void setStatus(TaskStatus status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public RenameStrategy getStrategy() {
        return this.strategy;
    }

    public List<ProcessingFile> getProcessibleFiles() {
        return this.processibleFiles;
    }

    public void results() {
        System.out.println("\n📋 Rapport de traitement pour : " + folderPath + " - stratégie : " + strategy.getClass().getSimpleName());
        System.out.println("📋 Status : " + status + " - message : " + statusMessage);
        System.out.println("===================================");

        if (this.files == null || this.files.length == 0) {
            System.out.println("\n⚠ Aucun fichier à analyser dans le dossier.");
            return;
        }

        List<ProcessingFile> renamedFiles = processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.PROCESSED)
                .toList();

        System.out.println("\n✅ Fichiers renommés : " + renamedFiles.size());
        if (renamedFiles.isEmpty()) {
            System.out.println("⚠ Aucun fichier renommé.");
        } else {
            renamedFiles.forEach(file
                    -> System.out.println(" - " + file.getOriginalName() + " → " + file.getFile().getName())
            );
        }

        List<ProcessingFile> failedFiles = processibleFiles.stream()
                .filter(file -> file.getStatus() == FileStatus.UNPROCESSABLE)
                .toList();

        System.out.println("\n❌ Fichiers non renommés : " + failedFiles.size());
        if (failedFiles.isEmpty()) {
            System.out.println("✅ Aucun échec.");
        } else {
            failedFiles.forEach(file
                    -> System.out.println(" - " + file.getOriginalName() + " ⚠ " + file.getStatusMessage())
            );
        }

        System.out.println("\n📁 Total de fichiers analysés : " + (this.files == null ? "0" : this.files.length));
        System.out.println("📄 Fichiers traitables : " + processibleFiles.size());
        System.out.println("🚫 Fichiers ignorés : " + unprocessibleFiles.size());

        if (!unprocessibleFiles.isEmpty()) {
            System.out.println("\n🚫 Liste des fichiers ignorés :");
            unprocessibleFiles.forEach(file
                    -> System.out.println(" - " + file.getOriginalName() + " ⚠ " + file.getStatusMessage())
            );
        }
    }

}
