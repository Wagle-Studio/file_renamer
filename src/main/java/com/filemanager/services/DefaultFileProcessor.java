package com.filemanager.services;

import java.io.File;
import java.util.List;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.utils.FileUtils;

public class DefaultFileProcessor implements FileProcessor {

    @Override
    public void analyse(ProcessingTask task) {
        // Retrieve the folder content (list of files).
        List<File> folderContent = FileUtils.getFolderContent(task.getFolderPath());

        // If the folder is empty, stop processing.
        if (folderContent.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "The folder is empty.");
            task.results();
            return;
        }

        // Convert raw files to ProcessingFile objects.
        List<ProcessingFile> processedFiles = FileUtils.processingFiles(folderContent);

        // If no valid files are found, stop processing.
        if (processedFiles.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No files to process.");
            task.results();
            return;
        }

        // Validate files against the main strategy.
        List<ProcessingFile> processingFiles = FileUtils.applyStrategyFileValidation(processedFiles, task.getStrategy());

        // Validate files against the preprocess strategies (if any).
        if (!task.getStrategy().getPreprocess().isEmpty()) {
            for (RenameStrategy preStrategy : task.getStrategy().getPreprocess()) {
                processingFiles = FileUtils.applyStrategyFileValidation(processedFiles, preStrategy);
            }
        }

        // Categorize files into processable and unprocessable lists.
        task.setProcessibleFiles(FileUtils.getProcessableFiles(processingFiles));
        task.setUnprocessibleFiles(FileUtils.getUnprocessableFiles(processingFiles));

        // If no processable files remain, stop processing.
        if (task.getProcessibleFiles().isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No processable files.");
            task.results();
        }
    }

    @Override
    public void run(ProcessingTask task) {
        // Apply preprocessing strategies (if any).
        if (!task.getStrategy().getPreprocess().isEmpty()) {
            for (RenameStrategy preStrategy : task.getStrategy().getPreprocess()) {
                task.setProcessibleFiles(preStrategy.execute(task.getProcessibleFiles(), true));
            }
        }

        // Execute the main strategy.
        task.setProcessibleFiles(task.getStrategy().execute(task.getProcessibleFiles(), false));

        // Mark the task as successfully processed.
        task.setStatus(TaskStatus.PROCESSED, "Processed with success.");
        task.results();
    }
}
