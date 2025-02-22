package com.filemanager.gui.views;

import javafx.scene.Scene;

public interface View {

    Scene getScene();

    String getTitle();

    void build();
}
