package com.filemanager.gui.controllers;

import com.filemanager.gui.interactors.MainInteractor;
import com.filemanager.gui.views.MainView;
import com.filemanager.models.ProcessingTask;

public final class MainController extends BaseController {

    public MainController() {
        ProcessingTask task = new ProcessingTask();
        MainInteractor interactor = new MainInteractor(task, this::navigateToProcessController);
        MainView view = new MainView(interactor);
        this.render(view);
    }

    private void navigateToProcessController(ProcessingTask task) {
        new ProcessController(task);
    }
}
