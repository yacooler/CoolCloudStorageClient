package controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileManagerController{

    @FXML
    private VBox remoteFiles;

    @FXML
    private VBox localFiles;



    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnCopyAction(ActionEvent actionEvent) {
        FilePanelController remote = (FilePanelController) remoteFiles.getProperties().get("ctrl");
        FilePanelController local = (FilePanelController) localFiles.getProperties().get("ctrl");

        if (remote.getSelectedFileName() == null && local.getSelectedFileName() == null) return;

        FilePanelController source;
        FilePanelController destination;

        if (remote.getSelectedFileName() != null){
            source = remote;
            destination = local;
        } else {
            source = local;
            destination = remote;
        }

        Path sourcePath;
        Path destinationPath;

        sourcePath = Paths.get(source.getCurrentPath(), source.getSelectedFileName());
        destinationPath = Paths.get(destination.getCurrentPath());

        try {
            Files.copy(sourcePath, destinationPath.resolve(sourcePath.getFileName().toString()));
        } catch (IOException e) {
            //файл уже есть
            e.printStackTrace();
        }

        //Обновить панель
        destination.updateList(destinationPath);

    }
}
