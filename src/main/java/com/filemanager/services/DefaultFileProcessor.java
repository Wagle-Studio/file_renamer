package com.filemanager.services;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.RenameStrategyFactory;
import com.filemanager.services.renaming.StrategyType;
import com.filemanager.utils.FileUtils;

public class DefaultFileProcessor implements FileProcessor {

    @Override
    public ProcessingTask run(String folderPath, StrategyType strategyType) {
        Optional<File[]> folderContent = FileUtils.getFolderContent(folderPath);
        RenameStrategy strategy = RenameStrategyFactory.getStrategy(strategyType);

        if (folderContent.isEmpty()) {
            return new ProcessingTask(folderPath, strategy, "The folder is empty.");
        }

        List<ProcessingFile> processedFiles = FileUtils.ProcessingFiles(folderContent.get());

        if (processedFiles.isEmpty()) {
            return new ProcessingTask(folderPath, strategy, "No files to process.");
        }

        processedFiles.forEach(file -> {
            if (!strategy.validateFileMetadata(file.getMetadata())) {
                file.setStatus(FileStatus.UNPROCESSABLE, "Metadata doesn't match strategy requirements.");
            }
        });

        List<ProcessingFile> processibleFiles = FileUtils.getProcessableFiles(processedFiles);
        List<ProcessingFile> unprocessibleFiles = FileUtils.getUnprocessableFiles(processedFiles);

        ProcessingTask ProcessingTask = new ProcessingTask(folderPath, strategy, folderContent.get(), processibleFiles, unprocessibleFiles);

        ProcessingTask.getStrategy().execute(ProcessingTask.getProcessibleFiles());

        ProcessingTask.setStatus(TaskStatus.PROCESSED, "Task ended with success.");

        return ProcessingTask;
    }
}
