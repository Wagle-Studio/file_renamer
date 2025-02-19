package com.filemanager.services;

import com.filemanager.services.renaming.StrategyType;

public interface FileProcessor {

    public void run(String folderPath, StrategyType strategyType);
}
