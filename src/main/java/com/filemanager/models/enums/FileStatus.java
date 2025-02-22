package com.filemanager.models.enums;

public enum FileStatus {
    UNDEFINED("Undefined"),
    UNPROCESSABLE("Unprocessable"),
    PROCESSABLE("Processable"),
    PROCESSING("Processing"),
    PROCESSED("Processed");

    private final String displayValue;

    FileStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
