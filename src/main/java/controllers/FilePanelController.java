package controllers;

import fileobjects.FileInformation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FilePanelController implements Initializable {

    @FXML
    TableView<FileInformation> filesTable;

    @FXML
    TextField pathField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    private void initTable(){

        TableColumn<FileInformation, String> fileTypeColumn = new TableColumn<>();
        TableColumn<FileInformation, String> fileNameColumn = new TableColumn<>("Файл");
        TableColumn<FileInformation, Long> fileSizeColumn = new TableColumn<>("Размер");
        TableColumn<FileInformation, String> fileLastModifiedColumn = new TableColumn<>("Дата изменения");


        filesTable.getSortOrder().add(fileTypeColumn);

        fileTypeColumn.setCellValueFactory(fileInfo -> new SimpleStringProperty(fileInfo.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        fileNameColumn.setCellValueFactory(fileInfo -> new SimpleStringProperty(fileInfo.getValue().getName()));
        fileNameColumn.setPrefWidth(240);

        fileSizeColumn.setCellValueFactory(fileInfo -> new SimpleObjectProperty<>(fileInfo.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInformation, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item == -1L ? "[Директория]" : String.format("%,d байт", item));
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);


        fileLastModifiedColumn.setCellValueFactory(
                fileInfo -> new SimpleStringProperty(
                        fileInfo.getValue().getLastModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        fileLastModifiedColumn.setPrefWidth(200);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileLastModifiedColumn);


        updateList(Paths.get("."));
    }

    public void updateList(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInformation::new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Не удалось обновить список файлов каталога " + path.toString(),
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null){
            updateList(upperPath);
        }
    }

    public void tblMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            FileInformation selectedItem = filesTable.getSelectionModel().getSelectedItem();
            if (selectedItem.getType() == FileInformation.FileType.DIRECTORY) {
                Path path = Paths.get(pathField.getText()).resolve(selectedItem.getName());
                updateList(path);
            }
        }
    }

    public String getSelectedFileName(){
        if (!filesTable.isFocused()) return null;
        return filesTable.getSelectionModel().getSelectedItem().getName();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }

}

