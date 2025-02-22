package com.filemanager.gui.controllers;

import com.filemanager.core.Injector;
import com.filemanager.core.ViewManager;
import com.filemanager.gui.views.View;

public abstract class BaseController {

    private final ViewManager viewManager = Injector.getInstance().getViewManager();

    public void render(View view) {
        this.viewManager.changeScreen(view);
    }
}
