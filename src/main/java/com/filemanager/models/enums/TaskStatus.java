package com.filemanager.models.enums;

public enum TaskStatus {
    UNDEFINED("Undefined"),
    ANALYSING("Analysing"),
    ANALYSED("Analysed"),
    PROCESSING("Processing"),
    PROCESSED("Processed"),
    ERROR("Error");

    private final String displayValue;

    TaskStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}
