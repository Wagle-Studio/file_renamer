package com.filemanager.gui.interactors;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import com.filemanager.gui.models.StrategyChoice;
import com.filemanager.models.ProcessingTask;
import com.filemanager.services.renaming.RenameStrategy;
import com.filemanager.services.renaming.RenameStrategyFactory;
import com.filemanager.services.renaming.enums.FileExtension;
import com.filemanager.services.renaming.enums.StrategyType;

import javafx.collections.FXCollections;
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
        return FXCollections.observableArrayList(
                new StrategyChoice("Rename with a random name (UUID)", StrategyType.RANDOM),
                new StrategyChoice("Rename by original creation date", StrategyType.BY_DATE)
        );
    }

    public void handleStrategyChoice(StrategyChoice choice) {
        RenameStrategy strategy = RenameStrategyFactory.getStrategy(choice.getValue());
        this.task.setStrategy(strategy);
    }

    public void resetStrategyChoice() {
        this.task.setStrategy(null);
    }

    public String getSelectedStrategy() {
        return this.task.getStrategy().getDisplayName();
    }

    public FileExtension[] getExtensionChoices() {
        return FileExtension.values();
    }

    public void handleExtensionChoice(List<FileExtension> extensions) {
        this.task.setExtensions(extensions);
    }

    public List<FileExtension> getSelectedExtensions() {
        return this.task.getExtensions();
    }

    public Boolean analysisRequirementsAreValid() {
        Boolean validPathFolder = this.task.getFolderPath() != null;
        Boolean validStrategy = this.task.getStrategy() != null;
        Boolean validExtensions = !this.task.getExtensions().isEmpty();

        return (validPathFolder && validStrategy && validExtensions);
    }

    public void handleStartAnalyse() {
        this.onStrategySelectedCallback.accept(task);
    }
}
