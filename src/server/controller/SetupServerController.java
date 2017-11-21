package server.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import server.model.ServerModel;

import java.io.*;
import java.net.ServerSocket;


public class SetupServerController {

    @FXML
    private Button ButtonServerCreate;
    @FXML
    private TextField socketTextField;

    private ServerModel serverModel;
    private EchoServer echoServer;




    @FXML
    public void initialize(){
        serverModel = new ServerModel();
    }

    @FXML
    public void handleButtonClick(ActionEvent event) throws IOException{
        if(checkSocket()) {
            serverModel.setPort(Integer.parseInt(socketTextField.getText()));
            echoServer = new EchoServer(serverModel.getPort());
            hideMainStage(event);
        }
        else{
        }
    }

    private boolean checkSocket(){
        if(isInteger(socketTextField.getText())) {
            return true;
        }else {
            ButtonServerCreate.setText("It's string...");
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

    private void hideMainStage(ActionEvent event) throws IOException{
        ((Parent) (event.getSource())).getScene().getWindow().hide();
    }
}