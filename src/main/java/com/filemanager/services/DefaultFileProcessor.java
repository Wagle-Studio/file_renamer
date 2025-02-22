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
        task.setStatus(TaskStatus.ANALYSING, "Analysing folder.");

        List<File> folderContent = FileUtils.getFolderContent(task.getFolderPath());

        if (folderContent.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "The folder is empty.");
            return;
        }

        List<ProcessingFile> processedFiles = FileUtils.processingFiles(folderContent);

        if (processedFiles.isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No files to process.");
            return;
        }

        List<ProcessingFile> processingFiles = FileUtils.applyStrategyFileValidation(processedFiles, task.getStrategy());

        if (!task.getStrategy().getPreprocess().isEmpty()) {
            for (RenameStrategy preStrategy : task.getStrategy().getPreprocess()) {
                processingFiles = FileUtils.applyStrategyFileValidation(processedFiles, preStrategy);
            }
        }

        task.setProcessibleFiles(FileUtils.getProcessableFiles(processingFiles));
        task.setUnprocessibleFiles(FileUtils.getUnprocessableFiles(processingFiles));

        if (task.getProcessibleFiles().isEmpty()) {
            task.setStatus(TaskStatus.ERROR, "No processable files.");
            return;
        }

        task.setStatus(TaskStatus.ANALYSED, "Ready to process files.");
    }

    @Override
    public void run(ProcessingTask task) {
        if (!task.getStrategy().getPreprocess().isEmpty()) {
            for (RenameStrategy preStrategy : task.getStrategy().getPreprocess()) {
                List<ProcessingFile> processingFiles = preStrategy.execute(task.getProcessibleFiles(), true);
                task.setProcessibleFiles(FileUtils.getProcessableFiles(processingFiles));
            }
        }

        List<ProcessingFile> finalFiles = task.getStrategy().execute(task.getProcessibleFiles(), false);
        task.setProcessibleFiles(FileUtils.getProcessableFiles(finalFiles));
        task.setStatus(TaskStatus.PROCESSED, "Processed with success.");
    }
}
