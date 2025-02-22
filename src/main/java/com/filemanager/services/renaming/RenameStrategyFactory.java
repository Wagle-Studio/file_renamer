package com.filemanager.services.renaming;

import com.filemanager.services.renaming.strategies.RenameByDate;
import com.filemanager.services.renaming.strategies.RenameRandomly;

public class RenameStrategyFactory {

    public static RenameStrategy getStrategy(StrategyType type) {
        switch (type) {
            case RANDOM:
                return new RenameRandomly();
            case BY_DATE:
                return new RenameByDate();
            default:
                throw new IllegalArgumentException("Unknown rename strategy: " + type);
        }
    }
}
