package com.filemanager.models;

import java.io.File;
import java.util.List;

import com.filemanager.services.renaming.RenameStrategy;

public class ProcessingTask {

    private static final String STATUS_RUNNING = "STATUS_RUNNING";
    private static final String STATUS_SUCCESS = "STATUS_SUCCESS";
    private static final String STATUS_ERROR = "STATUS_ERROR";

    private String status;
    private String statusMessage;
    private String folderPath;
    private RenameStrategy strategy;
    private File[] files;
    private List<ProcessingFile> processibleFiles;
    private List<ProcessingFile> unprocessibleFiles;

    public ProcessingTask(String message) {
        this.status = ProcessingTask.STATUS_ERROR;
        this.statusMessage = message;
    }

    public ProcessingTask(
            String folderPath,
            RenameStrategy strategy,
            File[] files,
            List<ProcessingFile> processibleFiles,
            List<ProcessingFile> unprocessibleFiles
    ) {
        this.status = ProcessingTask.STATUS_RUNNING;
        this.folderPath = folderPath;
        this.strategy = strategy;
        this.files = files;
        this.processibleFiles = List.copyOf(processibleFiles);
        this.unprocessibleFiles = List.copyOf(unprocessibleFiles);
    }

    public void setError(String message) {
        this.status = ProcessingTask.STATUS_ERROR;
        this.statusMessage = message;
    }

    public void setSuccess(String message) {
        this.status = ProcessingTask.STATUS_SUCCESS;
        this.statusMessage = message;
    }

    public void results() {
        System.out.println("\n📋 Rapport de traitement pour : " + folderPath + " - stratégie : " + strategy.getClass());
        System.out.println("\n📋 Status : " + status + " - message : " + statusMessage);
        System.out.println("===================================");

        System.out.println("\n✅ Fichiers traités : " + this.processibleFiles.size());
        this.processibleFiles.forEach(file -> System.out.println(" - " + file.getFile().getName()));

        System.out.println("\n❌ Fichiers non traitables : " + this.unprocessibleFiles.size());
        this.unprocessibleFiles.forEach(file -> System.out.println(" - " + file.getFile().getName()));

        System.out.println("\n📁 Total de fichiers analysés : " + this.files.length);
    }

}
