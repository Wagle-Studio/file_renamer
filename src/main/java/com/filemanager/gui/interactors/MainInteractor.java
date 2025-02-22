package com.filemanager.gui.interactors;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import com.filemanager.gui.models.StrategyChoice;
import com.filemanager.models.ProcessingTask;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.RenameStrategyFactory;
import com.filemanager.services.renaming.StrategyType;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public final class MainInteractor {

    private final ProcessingTask task;
    private final Consumer<ProcessingTask> onStrategySelectedCallback;

    public MainInteractor(ProcessingTask task, Consumer<ProcessingTask> onStrategySelectedCallback) {
        this.task = task;
        this.onStrategySelectedCallback = onStrategySelectedCallback;
    }

    public void handleFileSearch(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionnez un dossier");

        File folder = directoryChooser.showDialog(ownerWindow);

        if (folder != null) {
            this.task.setFolderPath(folder.getAbsolutePath());
        }
    }

    public String getSelectedFolder() {
        return this.task.getFolderPath();
    }

    public List<StrategyChoice> getStrategyChoices() {
        ObservableList choices = FXCollections.observableArrayList(
                new StrategyChoice("Rename by original date", StrategyType.BY_DATE)
        );
        return new SimpleListProperty(choices);
    }

    public void handleStrategyChoice(StrategyChoice choice) {
        RenameStrategy strategy = RenameStrategyFactory.getStrategy(choice.getValue());
        this.task.setStrategy(strategy);
    }

    public void handleStartAnalyse() {
        this.onStrategySelectedCallback.accept(task);
    }
}
