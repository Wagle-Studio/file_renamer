package com.filemanager.gui.views;

import org.controlsfx.control.CheckComboBox;

import com.filemanager.gui.interactors.MainInteractor;
import com.filemanager.gui.models.ExtensionBox;
import com.filemanager.gui.models.StrategyChoice;
import com.filemanager.services.renaming.enums.FileExtension;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Window;

public final class MainView extends BaseView {

    public static final String TITLE = "FileManager";
    public static final String PATH = "main.fxml";
    private final MainInteractor interactor;
    private final StrategyChoice strategyPlaceholder = new StrategyChoice("Then, select a Strategy", null);
    private final ExtensionBox extensionPlaceholder = new ExtensionBox("And your target extensions", null);
    private boolean isFolderSelected = false;

    @FXML
    private Button buttonSelectFolder;
    @FXML
    private Label labelSelectedFolderPath;
    @FXML
    private ChoiceBox<StrategyChoice> choiceBoxSelectStrategy;
    @FXML
    private Label labelTaskStrategy;
    @FXML
    private Button buttonStartAnalyse;
    @FXML
    private CheckComboBox<FileExtension> comboBoxSelectedExtensions;

    public MainView(MainInteractor interactor) {
        super(PATH, TITLE);
        this.interactor = interactor;
    }

    @Override
    public void build() {
        this.initializeStrategyChoiceBox();
        this.initializeExtensionComboBox();
        buttonSelectFolder.setOnAction(event -> this.handleFileSearch());
        buttonStartAnalyse.setOnAction(event -> this.handleStartAnalyse());
    }

    private void handleFileSearch() {
        Window window = buttonSelectFolder.getScene().getWindow();
        this.interactor.handleFileSearch(window);

        String selectedFolder = this.interactor.getSelectedFolder();

        if (selectedFolder != null && !selectedFolder.isEmpty()) {
            this.isFolderSelected = true;
            labelSelectedFolderPath.setVisible(true);
            labelSelectedFolderPath.setText(selectedFolder);
        } else {
            this.isFolderSelected = false;
            labelSelectedFolderPath.setVisible(false);
        }

        updateStartButtonState();
    }

    private void initializeStrategyChoiceBox() {
        choiceBoxSelectStrategy.getItems().add(this.strategyPlaceholder);
        choiceBoxSelectStrategy.getItems().addAll(this.interactor.getStrategyChoices());
        choiceBoxSelectStrategy.getSelectionModel().select(this.strategyPlaceholder);

        choiceBoxSelectStrategy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(this.strategyPlaceholder)) {
                this.interactor.handleStrategyChoice(newValue);
                labelTaskStrategy.setVisible(true);
                labelTaskStrategy.setText(this.interactor.getSelectedStrategy());
            } else {
                labelTaskStrategy.setVisible(false);
            }

            updateStartButtonState();
        });
    }

    private void initializeExtensionComboBox() {
        comboBoxSelectedExtensions.getItems().addAll(FileExtension.values());

        comboBoxSelectedExtensions.getCheckModel().getCheckedItems().addListener((ListChangeListener<FileExtension>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    System.out.println("Extensions sélectionnées : " + comboBoxSelectedExtensions.getCheckModel().getCheckedItems());
                }
            }
        });
    }

    private void updateStartButtonState() {
        boolean isStrategySelected = !choiceBoxSelectStrategy.getSelectionModel().getSelectedItem().equals(this.strategyPlaceholder);
        buttonStartAnalyse.setDisable(!(this.isFolderSelected && isStrategySelected));
    }

    private void handleStartAnalyse() {
        this.interactor.handleStartAnalyse();
    }
}
