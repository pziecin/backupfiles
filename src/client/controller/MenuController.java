package client.controller;

import client.model.MenuModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Piotrek on 2017-10-03.
 */
public class MenuController {
    private int port;
    private EchoClient echoClient;
    private MenuModel menuModel;
    private ArrayList<String> fileName, filePath;
    private ArrayList<Long> fileSize;
    private int period;
    @FXML
    ProgressBar pbc, pbs;

    @FXML
    CheckBox checkboxsynchro;

    @FXML
    private ListView listviewclient, listviewserver;

    ScheduledExecutorService ses;

    @FXML
    public void initialize(){
        echoClient = new EchoClient();
        menuModel = new MenuModel();
        fileSize = new ArrayList<>();
        filePath = new ArrayList<>();
        fileName = new ArrayList<>();
        period = 10;
    }

    @FXML
    public void addNewFile(){
        openFileClient();
        pbc.setProgress(0);
    }

    @FXML
    public void sendFiles() {
        if(menuModel.getFilecounter()!=0) {
            clearListViews(listviewclient);
            sendFiles(menuModel.getFilecounter(), filePath, fileName, fileSize);
            try {
                echoClient.closeSocket();
            } catch (IOException e) {
                System.out.println("Nie udalo sie zamkniecie");
            }
        }
    }

    @FXML
    public void receiveFiles() {
        try {
            pbs.setProgress(0);
            echoClient.setSocket(port);
            echoClient.sendYouShouldSendFiles(false);
            echoClient.sendYouShouldSendFiles(true);
            echoClient.readCounter();
            if(echoClient.returnCounter()!=0) {
                echoClient.readFileName();
                echoClient.readFileSize();
                echoClient.saveFile(pbs);
            }
            echoClient.closeSocket();
        } catch (IOException ioe) {
        }
    }

    @FXML
    public void receiveFilesInfo(){
        try {
            echoClient.setSocket(port);
            receiveFilesNamesAndSizeFromServer();
        }catch(IOException ioe){}
    }

    private void receiveFilesNamesAndSizeFromServer(){
        try {
            echoClient.sendYouShouldSendFiles(true);
            echoClient.sendYouShouldSendFiles(false);
            echoClient.readCounter();
            if (echoClient.returnCounter() != 0) {
                echoClient.readFileName();
                echoClient.readFileSize();
                clearListViews(listviewserver);
                setListViews(echoClient.getFileNameList(), listviewserver);
            }
        }
        catch(IOException ioe) {

        }
    }

    @FXML
    public void synchronizedClientAndServer(){
        if(checkboxsynchro.isSelected()) {
            ses = Executors.newSingleThreadScheduledExecutor();
            runsynchro();
        }
        else {
            ses.shutdown();
        }
    }

    public void runsynchro() {
        ses.scheduleAtFixedRate(() -> {
                receiveFiles();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("I have a great message for you!");
                alert.show();
    }, 0 ,period,TimeUnit.SECONDS);
    }





    public void openFileClient(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select files to back up!");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter(".txt", "*.txt"),
                new FileChooser.ExtensionFilter(".png, .jpg, .gif", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter(".mp3, .wav, .aac", "*.wav", "*.mp3", "*.aac"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(new Stage());

        if(selectedFile != null) {
            int newFileCounter = menuModel.getFilecounter() + selectedFile.size();
            menuModel.setFilecounter(newFileCounter);
            int fileCounterInThisLoop = selectedFile.size();

            for (int i = 0; i < fileCounterInThisLoop; i++) {
                fileName.add(selectedFile.get(i).getName());
                filePath.add(selectedFile.get(i).getAbsolutePath());
                fileSize.add(selectedFile.get(i).length());
            }

            clearListViews(listviewclient);
            setListViews(fileName, listviewclient);
        }
    }

    @FXML
    public void deleteFileToSend(){
        int fileToDelete = listviewclient.getEditingIndex();
        if(fileToDelete != -1) {
            listviewclient.getItems().remove(fileToDelete);
            menuModel.setFilecounter(menuModel.getFilecounter()-1);
            fileName.remove(fileToDelete);
            filePath.remove(fileToDelete);
            fileSize.remove(fileToDelete);
        }
    }

    private void deleteFile(int fileToDelete){
            menuModel.setFilecounter(menuModel.getFilecounter()-1);
            fileName.remove(fileToDelete);
            filePath.remove(fileToDelete);
            fileSize.remove(fileToDelete);
    }


    private void setListViews(List<String> fileName, ListView list){
        if(fileName != null){
            for(int i =0; i < fileName.size(); i++) {
                list.getItems().add(fileName.get(i));
            }
        }
    }

    private void clearListViews(ListView listView){
        listView.getItems().clear();
    }

    private void sendFiles(int fileCounter,List<String> filePath, List<String> fileName, List<Long> fileSize){
        try {
            echoClient.setSocket(port);

//            receiveFilesNamesAndSizeFromServer();
//            for(int i = 0;i < echoClient.getFileNameList().size();i++)
//                for(int j =0;j < fileName.size();j++)
//                    if(echoClient.getFileNameList().get(i).equals(fileName.get(i)))
//                        deleteFile(i);

            echoClient.sendYouShouldSendFiles(false);
            echoClient.sendYouShouldSendFiles(false);

            echoClient.sendFileCounter(fileCounter);

            for (int i = 0; i < fileName.size(); i++) {
                echoClient.sendFileNames(fileName.get(i));
            }

            for (int i = 0; i < fileSize.size(); i++) {
                echoClient.sendFileSize(fileSize.get(i));
            }

            for (int i = 0; i < filePath.size(); i++) {
                echoClient.sendFile(filePath.get(i), fileSize.get(i),pbc);
            }
        }
        catch(Exception e){
            System.out.println("Soket w MenuKontroller jest wylaczony" + e);
        }
        finally {
            menuModel.setFilecounter(0);
            fileName.clear();
            filePath.clear();
            fileSize.clear();
        }
    }

    @FXML
    public void setSynchronizedTime(){
        TextInputDialog dialog = new TextInputDialog("sekundy");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");


        Optional<String> result = dialog.showAndWait();
        result.ifPresent(period -> period = result.get());

    }

    @FXML
    public void closeProgram(){

    }

    @FXML
    public void aboutInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Autor");
        alert.setHeaderText("Informacje o twórcy:");
        alert.setContentText("Program stworzony przez: Piotr Zięcina");

        alert.showAndWait();
    }




    public void setPort(int port) {
        this.port = port;
    }
}
