package com.filemanager.services.renaming.strategies;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.filemanager.models.ProcessingFile;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.services.renaming.BaseRenameStrategyWithParams;
import com.filemanager.services.renaming.enums.FileExtension;
import com.filemanager.services.renaming.enums.FormatPattern;
import com.filemanager.utils.FileUtils;

public class RenameByFormatPattern extends BaseRenameStrategyWithParams {

    private final String displayName = "Rename by format pattern";
    private FormatPattern formatPattern;

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

            Optional<String> fileName = FileUtils.getFileName(file.getFile());

            if (fileName.isEmpty()) {
                file.setStatus(FileStatus.UNPROCESSABLE, "File name is not processable");
                return;
            }

            String formattedExtension = extension.get().name().toLowerCase();
            String[] splitedFileName = fileName.get().split("[,\\.\\-_\\s]+");
            String[] formattedSplitedFileName = Arrays.stream(splitedFileName).map(String::toLowerCase).toArray(String[]::new);

            String newFileBaseName = this.getFileBaseName(formattedSplitedFileName);
            String newFileName = newFileBaseName + "." + formattedExtension;
            File newFile = new File(file.getParentDir(), newFileName);

            int suffix = nameCounter.getOrDefault(newFileBaseName, 0);

            while (newFile.exists()) {
                suffix++;
                String suffixSeparator = this.getFormatPattern() == FormatPattern.KEBAB_CASE ? "-" : "_";
                newFileName = newFileBaseName + suffixSeparator + suffix + "." + formattedExtension;
                newFile = new File(file.getParentDir(), newFileName);
            }

            if (file.getFile().renameTo(newFile)) {
                nameCounter.put(newFileName, suffix);
                file.setFile(newFile);
                String fileStatus = String.join(" ", "Renamed with", this.getFormatPattern().getDisplayValue(), "format");
                file.setStatus(prePreprocess ? FileStatus.PROCESSING : FileStatus.PROCESSED, fileStatus);
            } else {
                file.setStatus(FileStatus.UNPROCESSABLE, "Failed to rename file");
            }
        });

        return files;
    }

    private String getFileBaseName(String[] splittedFileName) {
        String newFileBaseName;

        switch (this.getFormatPattern()) {
            case FormatPattern.SNAKE_CASE ->
                newFileBaseName = this.formatInSnakeCase(splittedFileName);
            case FormatPattern.KEBAB_CASE ->
                newFileBaseName = this.formatInKebabCase(splittedFileName);
            case FormatPattern.CAMEL_CASE ->
                newFileBaseName = this.formatInCamelCase(splittedFileName);
            case FormatPattern.PASCAL_CASE ->
                newFileBaseName = this.formatInPascalCase(splittedFileName);
            default ->
                throw new IllegalArgumentException("Unknown format pattern : " + this.getFormatPattern());
        }

        return newFileBaseName;
    }

    private String formatInSnakeCase(String[] subStrings) {
        return String.join("_", subStrings);
    }

    private String formatInKebabCase(String[] subStrings) {
        return String.join("-", subStrings);
    }

    private String formatInCamelCase(String[] subStrings) {
        StringBuilder result = new StringBuilder(subStrings[0].toLowerCase());
        for (int i = 1; i < subStrings.length; i++) {
            result.append(capitalizeFirstLetter(subStrings[i]));
        }
        return result.toString();
    }

    private String formatInPascalCase(String[] subStrings) {
        StringBuilder result = new StringBuilder();
        for (String part : subStrings) {
            result.append(capitalizeFirstLetter(part));
        }
        return result.toString();
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public FormatPattern getFormatPattern() {
        return this.formatPattern;
    }

    public void setFormatPattern(FormatPattern formatPattern) {
        this.formatPattern = formatPattern;
    }
}
