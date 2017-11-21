package server.model;

import server.controller.EchoServer;

import java.util.ArrayList;
import java.util.List;

public class EchoServerModel {

    private String userCatalog;
    private List<String> fileNameList;
    private List<Long> fileSizeList;
    private int fileCounter;


    public EchoServerModel(){
        fileNameList = new ArrayList<>();
        fileSizeList = new ArrayList<>();
        userCatalog = ".\\userCatalog";
    }

    public int getFileCounter() {
        return fileCounter;
    }

    public void setFileCounter(int fileCounter) {
        this.fileCounter = fileCounter;
    }

    public String getUserCatalog() {
        return userCatalog;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public List<Long> getFileSizeList() {
        return fileSizeList;
    }
}
