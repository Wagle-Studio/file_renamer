package com.filemanager.gui.models;

import com.filemanager.services.renaming.enums.FileExtension;

public final class ExtensionBox {

    private final String key;
    private final FileExtension value;

    public ExtensionBox(String key, FileExtension value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public String getKey() {
        return this.key;
    }

    public FileExtension getValue() {
        return this.value;
    }
}
