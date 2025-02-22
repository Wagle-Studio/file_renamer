package com.filemanager.gui.views;

import com.filemanager.gui.interactors.MainInteractor;
import com.filemanager.gui.models.StrategyChoice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Window;

public final class MainView extends BaseView {

    public static final String TITLE = "main";
    public static final String PATH = "main.fxml";
    private final MainInteractor interactor;

    @FXML
    private Button buttonSelectFolder;
    @FXML
    private Label labelSelectedFolderPath;
    @FXML
    private ChoiceBox<StrategyChoice> choiceBoxSelectStrategy;
    @FXML
    private Button buttonStartAnalyse;

    public MainView(MainInteractor interactor) {
        super(PATH, TITLE);
        this.interactor = interactor;
    }

    @Override
    public void build() {
        this.initializeStrategyChoiceBox();
        buttonSelectFolder.setOnAction(event -> this.handleFileSearch());
        buttonStartAnalyse.setOnAction(event -> this.handleStartAnalyse());
    }

    private void handleFileSearch() {
        Window window = buttonSelectFolder.getScene().getWindow();
        this.interactor.handleFileSearch(window);
        labelSelectedFolderPath.setVisible(true);
        choiceBoxSelectStrategy.setVisible(true);
        labelSelectedFolderPath.setText(this.interactor.getSelectedFolder());
    }

    private void initializeStrategyChoiceBox() {
        this.choiceBoxSelectStrategy.getItems().addAll(this.interactor.getStrategyChoices());

        choiceBoxSelectStrategy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.interactor.handleStrategyChoice(newValue);
            buttonStartAnalyse.setVisible(true);
        });
    }

    private void handleStartAnalyse() {
        this.interactor.handleStartAnalyse();
    }
}
