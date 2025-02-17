package com.filemanager.core;

import com.filemanager.services.DefaultFileProcessor;
import com.filemanager.services.FileProcessor;

public class Injector {

    private static final Injector instance = new Injector();

    private final FileProcessor fileProcessor;

    private Injector() {
        this.fileProcessor = new DefaultFileProcessor();
    }

    public static Injector getInstance() {
        return instance;
    }

    public FileProcessor getFileProcessor() {
        return fileProcessor;
    }
}
