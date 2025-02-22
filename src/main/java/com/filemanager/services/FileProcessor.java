package com.filemanager.services;

import com.filemanager.models.ProcessingTask;

public interface FileProcessor {

    public void analyse(ProcessingTask task);
    public void run(ProcessingTask task);
}
