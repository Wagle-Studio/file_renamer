package com.filemanager.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class FileUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd");

    public static Optional<File[]> getFolderContent(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        return (files != null) ? Optional.of(files) : Optional.empty();
    }

    public static List<ProcessingFile> ProcessingFiles(File[] files) {
        List<ProcessingFile> processedFiles = new ArrayList<>();

        for (File file : files) {
            if (!file.isFile()) {
                processedFiles.add(new ProcessingFile(file, "Item is not a file."));
                continue;
            }

            Optional<Metadata> fileMetadata = FileUtils.getFileMetadata(file);

            if (fileMetadata.isEmpty()) {
                processedFiles.add(new ProcessingFile(file, "Metadata are empty."));
                continue;
            }

            processedFiles.add(new ProcessingFile(file, fileMetadata.get()));
        }

        return processedFiles;
    }

    public static Optional<Metadata> getFileMetadata(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            return Optional.ofNullable(metadata);
        } catch (ImageProcessingException | IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getFileExtension(Metadata metadata) {
        FileTypeDirectory fileTypeDirectory = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);

        return (fileTypeDirectory != null)
                ? Optional.ofNullable(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME))
                : Optional.empty();
    }

    public static Optional<String> getFileOriginalDate(Metadata metadata) {
        ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        return (exifDirectory != null && exifDirectory.getDateOriginal() instanceof Date)
                ? Optional.ofNullable(FileUtils.formatDate(exifDirectory.getDateOriginal()))
                : Optional.empty();
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
