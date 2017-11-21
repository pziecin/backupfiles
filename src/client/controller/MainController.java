package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Created by Piotrek on 2017-10-02.
 */
public class MainController {

    @FXML
    private StackPane mainStackPane;

    @FXML
    public void initialize(){
        loadLoginScene();
    }

    private void loadLoginScene() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../client/resources/LoginScene.fxml"));
        Pane pane = null;
        try {
             pane = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void setScreen(Pane pane) {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(pane);
    }
}
