package com.filemanager.gui.models;

import com.filemanager.services.renaming.StrategyType;

public final class StrategyChoice {

    private final String key;
    private final StrategyType value;

    public StrategyChoice(String key, StrategyType value) {
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

    public StrategyType getValue() {
        return this.value;
    }
}
