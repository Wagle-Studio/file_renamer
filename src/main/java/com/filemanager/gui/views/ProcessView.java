package com.filemanager.gui.views;

import com.filemanager.gui.interactors.ProcessInteractor;
import com.filemanager.models.ProcessingFile;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public final class ProcessView extends BaseView {

    public static final String TITLE = "FileManager";
    public static final String PATH = "process.fxml";
    private final ProcessInteractor interactor;
    private final ListProperty<ProcessingFile> displayedFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

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
    private Label labelDisplayedFiles;
    @FXML
    private CheckBox checkBoxHideUnprocessibleFiles;
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
    @FXML
    private Button buttonCancel;

    public ProcessView(ProcessInteractor interactor) {
        super(PATH, TITLE);
        this.interactor = interactor;
    }

    @Override
    public void build() {
        this.initializeLabels();
        this.initializeFileTable();

        if (this.interactor.getProcesibledFilesSize() > 1) {
            buttonStartProcess.setDisable(false);
        }

        this.interactor.getAllFiles().addListener((obs, oldList, newList) -> updateDisplayedFiles());

        checkBoxHideUnprocessibleFiles.setOnAction(event -> handleHideUnprocessableFiles());
        buttonStartProcess.setOnAction(event -> this.interactor.handleStartProcess());
        buttonCancel.setOnAction(event -> this.interactor.handleCancel());

        updateDisplayedFiles();
    }

    private void initializeLabels() {
        labelTaskStrategy.setText(this.interactor.getTaskStrategy());
        labelTaskStatusMessage.textProperty().bind(this.interactor.getTaskStatusMessage());
        labelFolderPath.setText(this.interactor.getFolderPath());
        labelProcessedFiles.setText(this.buildFilesSizeLabel(this.interactor.getAllFilesSize(), "analysed", "file"));
        labelProcessibleFiles.setText(this.buildFilesSizeLabel(this.interactor.getProcesibledFilesSize(), "processable", "file"));
        labelUnprocessibleFiles.setText(this.buildFilesSizeLabel(this.interactor.getUnprocesibledFilesSize(), "unprocessable", "file"));
        labelDisplayedFiles.textProperty().bind(this.displayedFiles.sizeProperty().asString().concat(" items"));
    }

    private String buildFilesSizeLabel(Integer filesSize, String filesType, String suffix) {
        return String.join(" ", Integer.toString(filesSize), filesType, filesSize > 1 ? (suffix + "s") : suffix);
    }

    private void initializeFileTable() {
        tableColumnInitialName.setCellValueFactory(new PropertyValueFactory<>("originalName"));
        tableColumnNewName.setCellValueFactory(new PropertyValueFactory<>("currentName"));
        tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().getDisplayValue()));
        tableColumnStatusMessage.setCellValueFactory(new PropertyValueFactory<>("statusMessage"));
        tableViewFile.itemsProperty().bind(this.displayedFiles);
    }

    private void handleHideUnprocessableFiles() {
        if (checkBoxHideUnprocessibleFiles.isSelected()) {
            this.displayedFiles.setAll(this.interactor.getProcesibledFiles());
        } else {
            this.displayedFiles.setAll(this.interactor.getAllFiles());
        }
    }

    private void updateDisplayedFiles() {
        if (checkBoxHideUnprocessibleFiles.isSelected()) {
            this.displayedFiles.setAll(this.interactor.getProcesibledFiles());
        } else {
            this.displayedFiles.setAll(this.interactor.getAllFiles());
        }
    }
}
