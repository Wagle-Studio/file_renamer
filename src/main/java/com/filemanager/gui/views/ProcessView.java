package com.filemanager.gui.views;

import com.filemanager.gui.interactors.ProcessInteractor;
import com.filemanager.models.ProcessingFile;

import javafx.beans.property.SimpleStringProperty;
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
    private Label labelTaskStrategy;
    @FXML
    private Label labelTaskStatusMessage;
    @FXML
    private Label labelFolderPath;
    @FXML
    private Label labelProcessedFiles;
    @FXML
    private Label labelProcessibleFiles;
    @FXML
    private Label labelUnprocessibleFiles;
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
        this.initializeLabels();
        this.initializeFileTable();

        buttonStartProcess.setOnAction(event -> this.interactor.handleStartProcess());

        if (this.interactor.getProcesibledFilesSize() > 1) {
            buttonStartProcess.setDisable(false);
        }
    }

    private void initializeLabels() {
        labelTaskStrategy.setText(this.interactor.getTaskStrategy());
        labelTaskStatusMessage.textProperty().bind(this.interactor.getTaskStatusMessage());
        labelFolderPath.setText(this.interactor.getFolderPath());
        labelProcessedFiles.setText(this.buildFilesSizeLabel(this.interactor.getAllFilesSize(), "analysed"));
        labelProcessibleFiles.setText(this.buildFilesSizeLabel(this.interactor.getProcesibledFilesSize(), "processable"));
        labelUnprocessibleFiles.setText(this.buildFilesSizeLabel(this.interactor.getUnprocesibledFilesSize(), "unprocessable"));
    }

    private String buildFilesSizeLabel(Integer filesSize, String filesType) {
        return String.join(" ", Integer.toString(filesSize), filesType, filesSize > 1 ? "files" : "file");
    }

    private void initializeFileTable() {
        tableColumnInitialName.setCellValueFactory(new PropertyValueFactory<>("originalName"));
        tableColumnNewName.setCellValueFactory(new PropertyValueFactory<>("currentName"));
        tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getDisplayValue()));
        tableColumnStatusMessage.setCellValueFactory(new PropertyValueFactory<>("statusMessage"));
        tableViewFile.itemsProperty().bind(this.interactor.getAllFiles());
    }
}
