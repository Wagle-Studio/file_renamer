package com.filemanager.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.RenameStrategyFactory;
import com.filemanager.services.renaming.StrategyType;
import com.filemanager.utils.FileUtils;

public class DefaultFileProcessor implements FileProcessor {

    @Override
    public ProcessingTask run(String folderPath, StrategyType strategyType) {
        RenameStrategy strategy = RenameStrategyFactory.getStrategy(strategyType);

        Optional<File[]> folderContent = FileUtils.getFolderContent(folderPath);

        if (folderContent.isEmpty()) {
            return new ProcessingTask("folder is empty");
        }

        List<ProcessingFile> processedFiles = this.processFiles(folderContent.get(), strategy);
        List<ProcessingFile> processibleFiles = processedFiles.stream().filter(ProcessingFile::isProcessable).toList();
        List<ProcessingFile> unprocessibleFiles = processedFiles.stream().filter(file -> !file.isProcessable()).toList();

        ProcessingTask processingTask = new ProcessingTask(folderPath, strategy, folderContent.get(), processibleFiles, unprocessibleFiles);

        processingTask.setSuccess("succes");
        return processingTask;
    }

    private List<ProcessingFile> processFiles(File[] files, RenameStrategy strategy) {
        List<ProcessingFile> processedFiles = new ArrayList<>();

        for (File file : files) {
            processedFiles.add(new ProcessingFile(strategy.validateFile(file), file));
        }

        return processedFiles;
    }
}
