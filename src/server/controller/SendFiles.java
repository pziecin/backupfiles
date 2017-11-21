package server.controller;

import server.model.EchoServerModel;

import java.io.*;

public class SendFiles {

    private EchoServerModel echoServerModel;

    public SendFiles(EchoServerModel echoServerModel){
        this.echoServerModel = echoServerModel;
    }

    public void sendFileCounter(PrintWriter pw) throws IOException {
        File folder = new File(echoServerModel.getUserCatalog());
        File[] listOfFiles = folder.listFiles();

        pw.println(listOfFiles.length);

    }

    public void sendFileNames(PrintWriter pw) throws IOException {
        File folder = new File(echoServerModel.getUserCatalog());
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            pw.println(listOfFiles[i].getName());
        }
    }

    public void sendFileSize(PrintWriter pw) throws IOException {
        File folder = new File(echoServerModel.getUserCatalog());
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            pw.println(listOfFiles[i].length());
        }

    }

    public void sendFile(DataOutputStream dos) throws IOException{
        File folder = new File(echoServerModel.getUserCatalog());
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            FileInputStream fis = new FileInputStream(listOfFiles[i].getAbsolutePath());
            byte[] buffer = new byte[4096];

            int read = 0;

            long remaining = listOfFiles[i].length();
            while ((read = fis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                remaining -= read;
                dos.write(buffer, 0, read);
            }

            dos.flush();

            fis.close();
        }
        dos.close();
    }
}
