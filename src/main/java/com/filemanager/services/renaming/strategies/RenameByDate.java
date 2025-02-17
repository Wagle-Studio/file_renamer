package com.filemanager.services.renaming.strategies;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.drew.metadata.Metadata;
import com.filemanager.models.ProcessingFile;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.utils.FileUtils;

public class RenameByDate implements RenameStrategy {

    @Override
    public Boolean validateFileMetadata(Metadata metadata) {
        if (metadata == null) {
            return false;
        }

        Optional<String> originalDate = FileUtils.getFileOriginalDate(metadata);
        Optional<String> extension = FileUtils.getFileExtension(metadata);

        return !(originalDate.isEmpty() || extension.isEmpty());
    }

    @Override
    public void execute(List<ProcessingFile> files) {
        Map<String, Integer> nameCounter = new HashMap<>();

        files.forEach(file -> {
            Optional<String> rawOriginalDate = FileUtils.getFileOriginalDate(file.getMetadata());
            Optional<String> rawExtension = FileUtils.getFileExtension(file.getMetadata());

            if (rawOriginalDate.isEmpty() || rawExtension.isEmpty()) {
                file.setStatus(FileStatus.UNPROCESSABLE, "Metadata doesn't match strategy requirements.");
                return;
            }

            File parentDir = file.getParentDir();

            if (parentDir == null) {
                file.setStatus(FileStatus.UNPROCESSABLE, "Parent directory is invalid.");
                return;
            }

            String originalDate = rawOriginalDate.get().toLowerCase();
            String extension = rawExtension.get().toLowerCase();

            String newFileName = originalDate + "." + extension;

            File newFile = new File(parentDir, newFileName);

            int suffix = nameCounter.getOrDefault(originalDate, 0);

            while (newFile.exists()) {
                suffix++;
                newFileName = originalDate + "_" + suffix + "." + extension;
                newFile = new File(parentDir, newFileName);
            }

            nameCounter.put(originalDate, suffix + 1);

            if (file.getFile().renameTo(newFile)) {
                file.setStatus(FileStatus.PROCESSED, "Renamed to " + newFileName);
            } else {
                file.setStatus(FileStatus.UNPROCESSABLE, "Failed to rename file.");
            }
        });
    }
}
