package com.filemanager.gui.views;

import java.io.File;

import com.filemanager.gui.interactors.MainInteractor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;

public final class MainView extends BaseView {

    public static final String TITLE = "main";
    public static final String PATH = "main.fxml";
    private final MainInteractor interactor;
    private final SimpleObjectProperty<File> folder = new SimpleObjectProperty<>();

    @FXML
    private Button buttonSelectFolder;
    @FXML
    private Label selectedFolderPath;

    public MainView(MainInteractor interactor) {
        super(PATH, TITLE);
        this.interactor = interactor;
    }

    @Override
    @FXML
    public void initialize() {
        selectedFolderPath.setVisible(false);

        buttonSelectFolder.setOnAction(event -> {
            Window window = buttonSelectFolder.getScene().getWindow();
            interactor.handleFileSearch(window);
            this.folder.set(interactor.getSelectedFolder().get());
            selectedFolderPath.setVisible(true);
            selectedFolderPath.setText(this.folder.get().getAbsolutePath());
        });
    }
}
