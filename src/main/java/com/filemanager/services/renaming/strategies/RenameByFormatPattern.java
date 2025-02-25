package com.filemanager.services.renaming.strategies;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.services.renaming.BaseRenameStrategyWithParams;
import com.filemanager.services.renaming.enums.FileExtension;
import com.filemanager.services.renaming.enums.FormatPattern;
import com.filemanager.utils.FileUtils;

public class RenameByFormatPattern extends BaseRenameStrategyWithParams {

    private final String displayName = "Rename by format pattern";

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Boolean validateStrategyParams() {
        return this.getFormatPattern() != null;
    }

    @Override
    public Boolean validateFileRequirements(ProcessingFile file) {
        File parentDir = file.getParentDir();

        if (parentDir == null) {
            return false;
        }

        Optional<FileExtension> extension = FileUtils.getFileExtension(file.getFile());

        return extension.isPresent();
    }

    @Override
    public List<ProcessingFile> execute(List<ProcessingFile> files, boolean prePreprocess) {
        files.forEach(file -> {
            Optional<FileExtension> extension = FileUtils.getFileExtension(file.getFile());

            if (extension.isEmpty()) {
                String fileStatus = "File doesn't match requirements for strategy : " + this.getDisplayName().toLowerCase();
                file.setStatus(FileStatus.UNPROCESSABLE, fileStatus);
                return;
            }

            Optional<String> fileName = FileUtils.getFileName(file.getFile());

            if (fileName.isEmpty()) {
                String fileStatus = "File name is not processable";
                file.setStatus(FileStatus.UNPROCESSABLE, fileStatus);
                return;
            }

            String regex = "[,\\.\\-_\\s]+";
            String[] splitedFileName = fileName.get().split(regex);
            String newFileName;

            switch (this.getFormatPattern()) {
                case FormatPattern.SNAKE_CASE ->
                    newFileName = this.formatInSnakeCase(splitedFileName);
                case FormatPattern.KEBAB_CASE ->
                    newFileName = this.formatInKebabCase(splitedFileName);
                default ->
                    // TODO: to handle.
                    throw new AssertionError();
            }

            System.out.println(newFileName);
        });

        return files;
    }

    private String formatInSnakeCase(String[] subStrings) {
        return String.join("_", subStrings);
    }

    private String formatInKebabCase(String[] subStrings) {
        return String.join("-", subStrings);
    }
}
