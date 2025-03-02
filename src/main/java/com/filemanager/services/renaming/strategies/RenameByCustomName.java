package com.filemanager.services.renaming.strategies;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.services.renaming.BaseRenameStrategyWithParams;
import com.filemanager.services.renaming.enums.FileExtension;
import com.filemanager.utils.FileUtils;

public class RenameByCustomName extends BaseRenameStrategyWithParams {

    private final String displayName = "Rename by custom name";
    private String customFileName;

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Boolean validateStrategyParams() {
        return this.getCustomFileName() != null;
    }

    @Override
    public Boolean validateFileRequirements(ProcessingFile file) {
        File parentDir = file.getParentDir();

        if (parentDir == null || !parentDir.exists() || !parentDir.isDirectory()) {
            return false;
        }

        return FileUtils.getFileExtension(file.getFile()).isPresent();
    }

    @Override
    public List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess) {
        Map<String, Integer> nameCounter = new HashMap<>();

        files.forEach(file -> {
            Optional<FileExtension> extension = FileUtils.getFileExtension(file.getFile());

            if (extension.isEmpty()) {
                String fileStatus = "File doesn't match requirements for strategy : " + this.getDisplayName().toLowerCase();
                file.setStatus(FileStatus.UNPROCESSABLE, fileStatus);
                return;
            }

            int suffix = nameCounter.getOrDefault(this.customFileName, 0);
            String newFileName = String.join(" ", this.customFileName, String.valueOf(suffix)) + "." + extension.get().name().toLowerCase();
            File newFile = new File(file.getParentDir(), newFileName);

            while (newFile.exists()) {
                suffix++;
                newFileName = String.join(" ", this.customFileName, String.valueOf(suffix)) + "." + extension.get().name().toLowerCase();
                newFile = new File(file.getParentDir(), newFileName);
            }

            if (file.getFile().renameTo(newFile)) {
                nameCounter.put(this.customFileName, suffix);
                file.setFile(newFile);
                file.setStatus(prePreprocess ? FileStatus.PROCESSING : FileStatus.PROCESSED, "Renamed with custom name");
            } else {
                file.setStatus(FileStatus.UNPROCESSABLE, "Failed to rename file");
            }
        });

        return files;
    }

    public String getCustomFileName() {
        return this.customFileName;
    }

    public void setCustomFileName(String customFileName) {
        this.customFileName = customFileName;
    }
}
