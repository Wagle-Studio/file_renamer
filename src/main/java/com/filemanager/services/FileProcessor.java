package com.filemanager.services;

import com.filemanager.models.ProcessingTask;

public interface FileProcessor {

    void analyse(ProcessingTask task);

    void run(ProcessingTask task);
}
