package com.filemanager.gui.controllers;

import com.filemanager.core.Injector;
import com.filemanager.gui.interactors.ProcessInteractor;
import com.filemanager.gui.views.ProcessView;
import com.filemanager.models.ProcessingTask;
import com.filemanager.services.FileProcessor;

public final class ProcessController extends BaseController {

    private final FileProcessor fileProcessor = Injector.getInstance().getFileProcessor();

    public ProcessController(ProcessingTask task) {
        ProcessInteractor interactor = new ProcessInteractor(task, this::processFiles, this::cancel);
        ProcessView view = new ProcessView(interactor);
        this.fileProcessor.analyse(task);
        this.render(view);
    }

    private void processFiles(ProcessingTask task) {
        this.fileProcessor.run(task);
    }

    private void cancel() {
        @SuppressWarnings("unused")
        MainController nextController = new MainController();
    }
}
