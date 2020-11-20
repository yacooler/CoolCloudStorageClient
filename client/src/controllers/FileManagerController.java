package controllers;

import fileobjects.FileInformation;
import fileobjects.FileList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import netty.NettyClient;

import java.nio.file.*;


public class FileManagerController{

    private NettyClient nettyClient;

    //Контроллеры удаленной и локальной панели
    FilePanelController remote;
    FilePanelController local;

    @FXML
    private VBox remoteFiles;

    @FXML
    private VBox localFiles;

    @FXML
    private ProgressBar progressBar;

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public void start(){
        remote = (FilePanelController) remoteFiles.getProperties().get("ctrl");
        local = (FilePanelController) localFiles.getProperties().get("ctrl");

        //Настроили обработку событий при входе в директорию в локальном и удаленном хранилище
        remote.onEnterDirectory(newPath ->{
            FileList fileList = nettyClient.getRemoteFileList(newPath.toString());
            remote.updateList(fileList);
        });

        local.onEnterDirectory(newPath -> {
            local.updateList(newPath);
        });

        remote.setOnExitFromDirectory(newPath -> {
            FileList fileList = nettyClient.getRemoteFileList(newPath.toString());
            remote.updateList(fileList);
        });

        local.setOnExitFromDirectory(newPath -> {
            //Если вышли на корневую директорию - покажем список дисков
            if (newPath.toString().isEmpty()) {
                local.filesTable.getItems().clear();
                FileInformation info;
                for (Path path:FileSystems.getDefault().getRootDirectories()) {
                    info = new FileInformation(path);
                    local.filesTable.getItems().add(info);
                }
            } else {
                local.updateList(newPath);
            }
        });


        FileList fileList = nettyClient.getRemoteFileList("");
        remote.updateList(fileList);
        local.updateList(Paths.get(""));
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnCopyAction(ActionEvent actionEvent) {

        if (remote.getSelectedFileName() == null && local.getSelectedFileName() == null) return;


        //Копируем файл с сервера на локальную машину
        if (remote.getSelectedFileName() != null){

            nettyClient.getRemoteFile(
                    remote.getCurrentPath() + remote.getSelectedFileName() ,
                    Paths.get(local.getCurrentPath()),
                    progress -> progressBar.setProgress(progress));

            local.updateList(Paths.get(local.getCurrentPath()));
        } else if(local.getSelectedFileName() != null){
            nettyClient.postFileRemote(
                    Paths.get(local.getCurrentPath(), local.getSelectedFileName()),
                    remote.getCurrentPath());

            remote.updateList(nettyClient.getRemoteFileList(remote.getCurrentPath()));

        }

//        FilePanelController source;
//        FilePanelController destination;
//
//        if (remote.getSelectedFileName() != null){
//            source = remote;
//            destination = local;
//        } else {
//            source = local;
//            destination = remote;
//        }
//
//        Path sourcePath;
//        Path destinationPath;
//
//        sourcePath = Paths.get(source.getCurrentPath(), source.getSelectedFileName());
//        destinationPath = Paths.get(destination.getCurrentPath());
//
//        try {
//            Files.copy(sourcePath, destinationPath.resolve(sourcePath.getFileName().toString()));
//        } catch (IOException e) {
//            //файл уже есть
//            e.printStackTrace();
//        }
//
//        //Обновить панель
//        destination.updateList(destinationPath);

    }
}
