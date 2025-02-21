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
                processingFile.setStatus(FileStatus.UNPROCESSABLE, "Item is not a file.");
                processedFiles.add(processingFile);
                continue;
            }

            Optional<Metadata> fileMetadata = FileUtils.getFileMetadata(file);

            if (fileMetadata.isEmpty()) {
                processingFile.setStatus(FileStatus.UNPROCESSABLE, "Metadata are empty.");
                processedFiles.add(processingFile);
                continue;
            }

            processingFile.setMetadata(fileMetadata.get());
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

    public static Optional<String> getFileExtension(Metadata metadata) {
        FileTypeDirectory fileTypeDirectory = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);

        return (fileTypeDirectory != null)
                ? Optional.of(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME))
                : Optional.empty();
    }

    public static Optional<String> getFileOriginalDate(Metadata metadata) {
        ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        return (exifDirectory != null && exifDirectory.getDateOriginal() instanceof Date)
                ? Optional.of(FileUtils.formatDate(exifDirectory.getDateOriginal()))
                : Optional.empty();
    }

    public static List<ProcessingFile> applyStrategyFileValidation(List<ProcessingFile> files, RenameStrategy strategy) {
        files.forEach(file -> {
            if (file.getStatus() == FileStatus.PROCESSABLE && !strategy.validateFileMetadata(file.getMetadata())) {
                file.setStatus(FileStatus.UNPROCESSABLE, "Metadata doesn't match strategy requirements.");
            }
        });
        return files;
    }

    public static List<ProcessingFile> getProcessableFiles(List<ProcessingFile> files) {
        return files.stream().filter(file -> file.getStatus() == FileStatus.PROCESSABLE).toList();
    }

    public static List<ProcessingFile> getUnprocessableFiles(List<ProcessingFile> files) {
        return files.stream().filter(file -> file.getStatus() == FileStatus.UNPROCESSABLE || file.getStatus() == FileStatus.UNDEFINED).toList();
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}
