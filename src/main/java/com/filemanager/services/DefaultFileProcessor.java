package com.filemanager.services;

import java.io.File;
import java.util.List;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.ProcessingTask;
import com.filemanager.models.enums.TaskStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.RenameStrategyFactory;
import com.filemanager.services.renaming.StrategyType;
import com.filemanager.utils.FileUtils;

public class DefaultFileProcessor implements FileProcessor {

    @Override
    public void run(String folderPath, StrategyType strategyType) {
        // Get the renaming strategy based on the given strategy type.
        RenameStrategy strategy = RenameStrategyFactory.getStrategy(strategyType);

        // Create a processing task with the specified folder and strategy.
        ProcessingTask task = new ProcessingTask();
        task.setFolderPath(folderPath);
        task.setStrategy(strategy);

        // Retrieve the folder content (list of files).
        List<File> folderContent = FileUtils.getFolderContent(task.getFolderPath());

        // If the folder is empty, stop processing.
        if (folderContent.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "The folder is empty.");
            task.results();
            return;
        }

        // Convert raw files to ProcessingFile objects.
        List<ProcessingFile> processingFiles = FileUtils.processingFiles(folderContent);

        // If no valid files are found, stop processing.
        if (processingFiles.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No files to process.");
            task.results();
            return;
        }

        // Validate files against the main renaming strategy.
        processingFiles = FileUtils.applyStrategyFileValidation(processingFiles, strategy);

        // Categorize files into processable and unprocessable lists.
        task.setProcessibleFiles(FileUtils.getProcessableFiles(processingFiles));
        task.setUnprocessibleFiles(FileUtils.getUnprocessableFiles(processingFiles));

        // If no processable files remain, stop processing.
        if (task.getProcessibleFiles().isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No processable files.");
            task.results();
            return;
        }

        // Apply preprocessing strategies (if any).
        for (RenameStrategy preStrategy : task.getStrategy().getPreprocess()) {
            // Validate files with the current preprocessing strategy.
            processingFiles = FileUtils.applyStrategyFileValidation(task.getProcessibleFiles(), preStrategy);

            // Update task with newly filtered processable/unprocessable files.
            task.setProcessibleFiles(FileUtils.getProcessableFiles(processingFiles));
            task.addUnprocessibleFiles(FileUtils.getUnprocessableFiles(processingFiles));

            // If no processable files remain after preprocessing, stop processing.
            if (task.getProcessibleFiles().isEmpty()) {
                task.setStatus(TaskStatus.ERROR, "No processable files remaining for strategy : " + preStrategy.getClass().getSimpleName());
                task.results();
                return;
            }

            // Execute preprocessing modifications.
            task.setProcessibleFiles(preStrategy.execute(task.getProcessibleFiles(), true));
        }

        // Final check before executing the main renaming strategy.
        if (task.getProcessibleFiles().isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No processable files remaining for strategy : " + task.getStrategy().getClass().getSimpleName());
            task.results();
            return;
        }

        // Execute the main renaming strategy.
        task.setProcessibleFiles(task.getStrategy().execute(task.getProcessibleFiles(), false));

        // Mark the task as successfully processed.
        task.setStatus(TaskStatus.PROCESSED, "Processed with success.");
        task.results();
    }
}
