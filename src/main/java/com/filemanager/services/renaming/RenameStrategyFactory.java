package com.filemanager.services.renaming;

import com.filemanager.services.renaming.strategies.RenameByDate;

public class RenameStrategyFactory {

    public static RenameStrategy getStrategy(StrategyType type) {
        switch (type) {
            case BY_DATE:
                return new RenameByDate();
            default:
                throw new IllegalArgumentException("Unknown rename strategy: " + type);
        }
    }
}
