package com.filemanager.gui.views;

import org.controlsfx.control.CheckComboBox;

import com.filemanager.gui.interactors.MainInteractor;
import com.filemanager.gui.models.StrategyChoice;
import com.filemanager.services.renaming.enums.FileExtension;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public final class MainView extends BaseView {

    public static final String TITLE = "FileManager";
    public static final String PATH = "main.fxml";
    private final MainInteractor interactor;

    @FXML
    private VBox dropBox;
    @FXML
    private Label labelSelectedFolderPath;
    @FXML
    private ChoiceBox<StrategyChoice> choiceBoxSelectStrategy;
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
        dropBox.setOnMouseClicked(event -> this.handleFileSearch());
        buttonStartAnalyse.setOnAction(event -> this.handleStartAnalyse());
    }

    private void handleFileSearch() {
        Window window = dropBox.getScene().getWindow();
        this.interactor.handleFileSearch(window);
        String selectedFolder = this.interactor.getSelectedFolder();

        if (selectedFolder != null && !selectedFolder.isEmpty()) {
            labelSelectedFolderPath.setVisible(true);
            labelSelectedFolderPath.setText(selectedFolder);
        } else {
            labelSelectedFolderPath.setVisible(false);
        }

        updateStartButtonState();
    }

    private void initializeStrategyChoiceBox() {
        StrategyChoice strategyPlaceholder = new StrategyChoice("Select a treatment for you files", null);

        choiceBoxSelectStrategy.getItems().add(strategyPlaceholder);
        choiceBoxSelectStrategy.getItems().addAll(this.interactor.getStrategyChoices());
        choiceBoxSelectStrategy.getSelectionModel().select(strategyPlaceholder);

        choiceBoxSelectStrategy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(strategyPlaceholder)) {
                this.interactor.handleStrategyChoice(newValue);
            } else {
                this.interactor.resetStrategyChoice();
            }

            updateStartButtonState();
        });
    }

    private void initializeExtensionComboBox() {
        comboBoxSelectedExtensions.getItems().addAll(this.interactor.getExtensionChoices());

        comboBoxSelectedExtensions.getCheckModel().getCheckedItems().addListener((ListChangeListener<FileExtension>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    this.interactor.handleExtensionChoice(comboBoxSelectedExtensions.getCheckModel().getCheckedItems());
                }
            }

            updateStartButtonState();
        });
    }

    private void updateStartButtonState() {
        buttonStartAnalyse.setDisable(!(this.interactor.analysisRequirementsAreValid()));
    }

    private void handleStartAnalyse() {
        this.interactor.handleStartAnalyse();
    }
}
