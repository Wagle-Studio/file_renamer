package com.filemanager.gui.interactors;

import java.io.File;
import java.util.Optional;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public final class MainInteractor {

    private File selectedFolder;

    public void handleFileSearch(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionnez un dossier");

        File folder = directoryChooser.showDialog(ownerWindow);

        if (folder != null) {
            this.selectedFolder = folder;
        }
    }

    public Optional<File> getSelectedFolder() {
        return Optional.ofNullable(this.selectedFolder);
    }
}
