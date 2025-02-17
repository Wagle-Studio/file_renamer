package com.filemanager.services;

import com.filemanager.models.ProcessingTask;
import com.filemanager.services.renaming.StrategyType;

public interface FileProcessor {

    public ProcessingTask run(String folderPath, StrategyType strategyType);
}
