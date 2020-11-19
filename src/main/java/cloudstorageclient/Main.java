package cloudstorageclient;

import controllers.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netty.NettyClient;

import java.io.IOException;

public class Main extends Application {

    private NettyClient nettyClient;

    private void fileManagerStage(Stage primaryStage){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/filemanager.fxml"));
            primaryStage.setTitle("File manager");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить сцену файлменеджера", e);
        }

    };

    private boolean loginStage(Stage primaryStage){
        boolean result = false;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage login = new Stage();

            Parent root = fxmlLoader.load();

            LoginController controller = fxmlLoader.getController();
            controller.setNettyClient(nettyClient);

            login.initOwner(primaryStage);
            login.setScene(new Scene(root, 440, 200));
            login.setAlwaysOnTop(true);
            login.initStyle(StageStyle.UTILITY);
            login.initModality(Modality.APPLICATION_MODAL);
            login.showAndWait();
            result = controller.getResult();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить сцену окна входа", e);
        }
        return result;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        nettyClient = new NettyClient("localhost", 8189);
        new Thread(nettyClient).start();

        fileManagerStage(primaryStage);
        if (!loginStage(primaryStage)){
            Platform.exit();
        };
    }




    public static void main(String[] args) {
        launch(args);
    }
}
