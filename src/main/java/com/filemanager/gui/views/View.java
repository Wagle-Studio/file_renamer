package com.filemanager.gui.views;

import javafx.scene.Scene;

public interface View {

    public Scene getScene();

    public String getTitle();

    public abstract void initialize();
}
