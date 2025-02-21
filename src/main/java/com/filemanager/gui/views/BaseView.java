package com.filemanager.gui.views;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class BaseView implements View {

    private static final String BASE_PATH = "/fxml/";
    private final String title;
    private final Scene scene;

    public BaseView(String fxmlFile, String title) {
        this.title = title;
        this.scene = loadFXML(fxmlFile);
    }

    private Scene loadFXML(String fxmlFile) {
        try {
            URL fxmlLocation = getClass().getResource(BASE_PATH + fxmlFile);
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML file not found: " + BASE_PATH + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            Parent root = loader.load();
            return new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML: " + BASE_PATH + fxmlFile, e);
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public abstract void initialize();
}
