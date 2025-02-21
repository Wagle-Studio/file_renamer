package com.filemanager.core;

import com.filemanager.services.DefaultFileProcessor;
import com.filemanager.services.FileProcessor;

public class Injector {

    private static final Injector instance = new Injector();

    private final ViewManager viewManager;
    private final FileProcessor fileProcessor;

    private Injector() {
        this.viewManager = new ViewManager();
        this.fileProcessor = new DefaultFileProcessor();
    }

    public static Injector getInstance() {
        return instance;
    }

    public ViewManager getViewManager() {
        return this.viewManager;
    }

    public FileProcessor getFileProcessor() {
        return this.fileProcessor;
    }
}
