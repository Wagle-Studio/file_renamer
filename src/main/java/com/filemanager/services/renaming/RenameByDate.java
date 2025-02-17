package com.filemanager.services.renaming;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.filemanager.models.ProcessingFile;
import com.filemanager.utils.FileUtils;

public class RenameByDate implements RenameStrategy {

    @Override
    public Boolean validateFile(File file) {
        if (!file.isFile()) {
            return false;
        }

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            Optional<String> originalDate = FileUtils.getFileOriginalDate(metadata);
            Optional<String> extension = FileUtils.getFileExtension(metadata);

            return !(originalDate.isEmpty() || extension.isEmpty());
        } catch (ImageProcessingException | IOException e) {
            return false;
        }
    }

    @Override
    public String rename(ProcessingFile file) {
        return "WIP";
    }
}
