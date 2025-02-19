package com.filemanager;

import com.filemanager.core.Injector;
import com.filemanager.services.FileProcessor;
import com.filemanager.services.renaming.StrategyType;

public class App {

    private final FileProcessor fileProcessor;

    public App(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    public static void main(String[] args) {
        FileProcessor fileProcessor = Injector.getInstance().getFileProcessor();

        App app = new App(fileProcessor);

        app.fileProcessor.run("/home/kevin/Documents/2013 Photos/2013 Barcarès", StrategyType.BY_DATE);
    }
}
