package client.model;

import java.util.ArrayList;
import java.util.List;

public class EchoClientModel {
    private String userCatalog;
    private List<String> fileNameList;
    private List<Long> fileSizeList;
    private int fileCounter;

    public EchoClientModel(){
        fileNameList = new ArrayList<>();
        fileSizeList = new ArrayList<>();
        userCatalog = ".\\FilesFromServer";
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
