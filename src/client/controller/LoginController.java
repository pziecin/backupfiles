package client.controller;

import client.model.LoginModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class LoginController{

    @FXML
    private Button ButtonLogIn;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField socketTextField;

    private LoginModel loginModel;

    @FXML
    public void initialize() {
        loginModel = new LoginModel();
    }

    @FXML
    public void handleButtonClick(ActionEvent event) throws IOException{
        if(loginModel.getPortFromFile()){
                setMainStage(event);
        }else{
            if(checkPassword() && checkSocket());
                loginModel.savePort();
                setMainStage(event);
        }
    }

    private boolean checkSocket(){
        if(isInteger(socketTextField.getText())) {
            if(Integer.parseInt(socketTextField.getText()) != 0) {
                try {
                    loginModel.setPort(Integer.parseInt(socketTextField.getText()));
                    //echoClient = new EchoClient(loginModel.getPort());
                    return true;
                } catch (Exception e) {
                    System.out.println("Client Exception " + e);
                    ButtonLogIn.setText("Wrong Port");
                    return false;
                }
            } else {
                return false;
            }
        }else {
            ButtonLogIn.setText("It's string...");
            return false;
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void exit(){
        Platform.exit();
    }

    private boolean checkPassword(){
        if(usernameTextField.getText().equals(loginModel.getNick()) && passwordField.getText().equals(loginModel.getPassword())){
            return true;
        }
        else
            ButtonLogIn.setText("Login error");
            return false;
    }

    private void setMainStage(ActionEvent event) throws IOException{
        ((Parent) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../client/resources/MenuScene.fxml"));
        Parent root = loader.load();
        MenuController controller = loader.getController();
        controller.setPort(loginModel.getPort());
        Stage window = new Stage();
        window.setTitle("Backuper");
        window.setScene(new Scene(root));
        window.setMinWidth(1024);
        window.setMinHeight(640);
        window.show();
    }
}