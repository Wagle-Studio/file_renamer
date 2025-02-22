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

    private final String displayName = "Rename by date";

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

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
    public List<RenameStrategy> getPreprocess() {
        return List.of(new RenameRandomly());
    }

    @Override
    public List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess) {
        Map<String, Integer> nameCounter = new HashMap<>();

        files.forEach(file -> {
            Optional<String> rawOriginalDate = FileUtils.getFileOriginalDate(file.getMetadata());
            Optional<String> rawExtension = FileUtils.getFileExtension(file.getMetadata());

            if (rawOriginalDate.isEmpty() || rawExtension.isEmpty()) {
                String fileStatus = "Metadata doesn't match requirements for strategy : " + this.getDisplayName().toLowerCase();
                file.setStatus(FileStatus.UNPROCESSABLE, fileStatus);
                return;
            }

            File parentDir = file.getParentDir();

            if (parentDir == null) {
                file.setStatus(FileStatus.UNPROCESSABLE, "Parent directory is invalid");
                return;
            }

            String originalDate = rawOriginalDate.get().toLowerCase();
            String extension = rawExtension.get().toLowerCase();

            int suffix = nameCounter.getOrDefault(originalDate, 0);

            String newFileName = originalDate + "_" + suffix + "." + extension;
            File newFile = new File(parentDir, newFileName);

            while (newFile.exists()) {
                suffix++;
                newFileName = originalDate + "_" + suffix + "." + extension;
                newFile = new File(parentDir, newFileName);
            }

            if (file.getFile().renameTo(newFile)) {
                nameCounter.put(originalDate, suffix);
                file.setFile(newFile);
                file.setStatus(prePreprocess ? FileStatus.PROCESSING : FileStatus.PROCESSED, "Renamed by date");
            } else {
                file.setStatus(FileStatus.UNPROCESSABLE, "Failed to rename file");
            }
        });

        return files;
    }
}
