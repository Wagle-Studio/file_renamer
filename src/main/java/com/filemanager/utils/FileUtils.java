package com.filemanager.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.filemanager.models.ProcessingFile;
import com.filemanager.models.enums.FileStatus;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.enums.FileExtension;

public class FileUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd");

    public static List<File> getFolderContent(String folderPath) {
        File folder = new File(folderPath);

        return Arrays.asList(folder.listFiles());
    }

    public static List<ProcessingFile> processingFiles(List<File> files) {
        List<ProcessingFile> processedFiles = new ArrayList<>();

        for (File file : files) {
            ProcessingFile processingFile = new ProcessingFile(file);

            if (!file.isFile()) {
                processingFile.setStatus(FileStatus.UNPROCESSABLE, "Item is not a file");
                processedFiles.add(processingFile);
                continue;
            }

            processingFile.setStatus(FileStatus.PROCESSABLE, "");
            processedFiles.add(processingFile);
        }

        return processedFiles;
    }

    public static Optional<Metadata> getFileMetadata(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            return Optional.of(metadata);
        } catch (ImageProcessingException | IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getFileName(File file) {
        String fileNameWithExtension = file.getName();
        int lastDotIndex = fileNameWithExtension.lastIndexOf(".");

        if (lastDotIndex <= 0 || lastDotIndex == fileNameWithExtension.length() - 1) {
            return Optional.empty();
        }

        String fileName = fileNameWithExtension.substring(0, lastDotIndex).toUpperCase();

        return Optional.of(fileName);
    }

    public static Optional<FileExtension> getFileExtension(File file) {
        Optional<FileExtension> extension = FileUtils.getFileExtensionByFile(file);

        if (extension.isEmpty()) {
            Optional<Metadata> metadata = FileUtils.getFileMetadata(file);

            if (metadata.isEmpty()) {
                return Optional.empty();
            }

            extension = FileUtils.getFileExtensionByMetadata(metadata.get());

            if (extension.isEmpty()) {
                return Optional.empty();
            }
        }

        return extension;
    }

    public static Optional<FileExtension> getFileExtensionByFile(File file) {
        String fileNameWithExtension = file.getName();
        int lastDotIndex = fileNameWithExtension.lastIndexOf(".");

        if (lastDotIndex <= 0 || lastDotIndex == fileNameWithExtension.length() - 1) {
            return Optional.empty();
        }

        String extension = fileNameWithExtension.substring(lastDotIndex + 1).toUpperCase();

        return FileExtension.fromString(extension);
    }

    public static Optional<FileExtension> getFileExtensionByMetadata(Metadata metadata) {
        FileTypeDirectory fileTypeDirectory = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);

        if (fileTypeDirectory == null) {
            return Optional.empty();
        }

        String extension = fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME);

        return FileExtension.fromString(extension);
    }

    public static Optional<String> getFileOriginalDateByMetaData(Metadata metadata) {
        ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        if (exifDirectory == null) {
            return Optional.empty();
        }

        Date originalDate = exifDirectory.getDateOriginal();

        if (!(originalDate instanceof Date)) {
            return Optional.empty();
        }

        return Optional.of(FileUtils.formatDate(originalDate));
    }

    public static List<ProcessingFile> applyFilterByExtension(List<ProcessingFile> files, List<FileExtension> extensions) {
        files.forEach(file -> {
            if (file.getStatus() != FileStatus.UNPROCESSABLE) {
                Optional<FileExtension> fileExtension = FileUtils.getFileExtension(file.getFile());

                if (fileExtension.isEmpty()) {
                    file.setStatus(FileStatus.UNPROCESSABLE, "Undefined file extension");
                }

                if (fileExtension.isPresent() && !extensions.contains(fileExtension.get())) {
                    file.setStatus(FileStatus.UNAFFECTED, "Not affected by the treatment");
                }
            }
        });
        return files;
    }

    public static List<ProcessingFile> applyStrategyFileValidation(List<ProcessingFile> files, RenameStrategy strategy) {
        files.forEach(file -> {
            if (file.getStatus() == FileStatus.PROCESSABLE && !strategy.validateFileRequirements(file)) {
                String fileStatus = "File doesn't match requirements for strategy : " + strategy.getDisplayName().toLowerCase();
                file.setStatus(FileStatus.UNPROCESSABLE, fileStatus);
            }
        });
        return files;
    }

    public static List<ProcessingFile> getProcessableFiles(List<ProcessingFile> files) {
        return files.stream().filter(file
                -> file.getStatus() == FileStatus.PROCESSABLE
                || file.getStatus() == FileStatus.PROCESSING
                || file.getStatus() == FileStatus.PROCESSED
        ).toList();
    }

    public static List<ProcessingFile> getUnprocessableFiles(List<ProcessingFile> files) {
        return files.stream().filter(file
                -> file.getStatus() == FileStatus.UNPROCESSABLE
                || file.getStatus() == FileStatus.UNDEFINED
                || file.getStatus() == FileStatus.UNAFFECTED
        ).toList();
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}
