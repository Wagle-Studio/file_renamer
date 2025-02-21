package com.filemanager;

import com.filemanager.core.Injector;
import com.filemanager.core.ViewManager;
import com.filemanager.gui.controllers.MainController;

import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {

    private final ViewManager screenManager = Injector.getInstance().getViewManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        screenManager.setStage(primaryStage);
        new MainController();
    }
}
