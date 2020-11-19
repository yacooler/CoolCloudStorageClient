package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import netty.NettyClient;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    NettyClient nettyClient;

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    boolean logged;

    @FXML // fx:id="passwordTextField"
    private PasswordField passwordTextField;

    @FXML // fx:id="loginButton"
    private Button loginButton;

    @FXML // fx:id="loginTextField"
    private TextField loginTextField;

    @FXML
    private Label messageLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginTextField.requestFocus();

    }

    public void setMessageText(String text){
        messageLabel.setText(text);
    }

    @FXML
    void doLogin(ActionEvent event) {

        //Проверили поля на заполненность
        if (loginTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()){
            setMessageText("Введите логин и пароль!");
            return;
        }

        //Пробуем авторизоваться
        if (nettyClient.doAuthorization(loginTextField.getText(),  passwordTextField.getText())) {
            setMessageText("Авторизация прошла успешно!");
        } else {
            setMessageText("Проверьте правильность логина и пароля!");
            return;
        };

        logged = true;
        ((Node) event.getSource()).getScene().getWindow().hide();

    }

    public boolean getResult(){
        return logged;
    }

}
