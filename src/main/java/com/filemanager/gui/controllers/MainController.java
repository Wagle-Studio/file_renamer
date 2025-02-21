package com.filemanager.gui.controllers;

import com.filemanager.gui.interactors.MainInteractor;
import com.filemanager.gui.views.MainView;

public final class MainController extends BaseController {

    public MainController() {
        MainInteractor interactor = new MainInteractor();
        MainView view = new MainView(interactor);
        this.render(view);
    }
}
