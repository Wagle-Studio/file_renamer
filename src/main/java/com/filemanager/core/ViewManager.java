package com.filemanager.core;

import com.filemanager.gui.views.View;

import javafx.stage.Stage;

public class ViewManager {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void changeScreen(View view) {
        if (this.stage == null) {
            throw new Error("Screen stage isn't defined.");
        }

        this.stage.setScene(view.getScene());
        this.stage.setTitle(view.getTitle());
        this.stage.show();

        view.initialize();
    }
}
