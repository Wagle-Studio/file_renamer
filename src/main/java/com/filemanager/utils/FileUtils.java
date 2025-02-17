package com.filemanager.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileTypeDirectory;

public class FileUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd");

    public static Optional<File[]> getFolderContent(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        return (files != null) ? Optional.of(files) : Optional.empty();
    }

    public static Optional<String> getFileOriginalDate(Metadata metadata) {
        ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        return (exifDirectory != null && exifDirectory.getDateOriginal() instanceof Date)
                ? Optional.ofNullable(FileUtils.formatDate(exifDirectory.getDateOriginal()))
                : Optional.empty();
    }

    public static Optional<String> getFileExtension(Metadata metadata) {
        FileTypeDirectory fileTypeDirectory = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);

        return (fileTypeDirectory != null)
                ? Optional.ofNullable(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).toLowerCase())
                : Optional.empty();
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}
