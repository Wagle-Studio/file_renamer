package com.filemanager.gui.views;

import com.filemanager.gui.interactors.ProcessInteractor;
import com.filemanager.models.ProcessingFile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public final class ProcessView extends BaseView {

    public static final String TITLE = "Process";
    public static final String PATH = "process.fxml";

    private final ProcessInteractor interactor;

    @FXML
    private Label labelStrategy;
    @FXML
    private Label labelFolderPath;
    @FXML
    private TableView<ProcessingFile> tableViewFile;
    @FXML
    private TableColumn<ProcessingFile, String> tableColumnInitialName;
    @FXML
    private TableColumn<ProcessingFile, String> tableColumnNewName;
    @FXML
    private TableColumn<ProcessingFile, String> tableColumnStatus;
    @FXML
    private TableColumn<ProcessingFile, String> tableColumnStatusMessage;
    @FXML
    private Button buttonStartProcess;

    public ProcessView(ProcessInteractor interactor) {
        super(PATH, TITLE);
        this.interactor = interactor;
    }

    @Override
    public void build() {
        labelStrategy.setText(this.interactor.getStrategy());
        labelFolderPath.setText(this.interactor.getFolderPath());
        this.initializeFileTable();
        buttonStartProcess.setOnAction(event -> this.interactor.handleStartProcess());
    }

    private void initializeFileTable() {
        tableColumnInitialName.setCellValueFactory(new PropertyValueFactory<>("originalName"));
        tableColumnNewName.setCellValueFactory(new PropertyValueFactory<>("currentName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnStatusMessage.setCellValueFactory(new PropertyValueFactory<>("statusMessage"));
        tableViewFile.setItems(this.interactor.getProcessedFiles());
    }
}
