package server.controller;

import server.model.EchoServerModel;

import java.io.*;

public class ReceiveFiles {
    private EchoServerModel echoServerModel;


    public ReceiveFiles(EchoServerModel echoServerModel){
        this.echoServerModel = echoServerModel;
    }

    public boolean readShouldISendFiles(BufferedReader br) throws IOException{
        boolean iShouldSendFiles =  Boolean.parseBoolean(br.readLine());
        return iShouldSendFiles;
    }

    public int readCounter(BufferedReader br) throws IOException {
        int counter = Integer.parseInt(br.readLine());
        return counter;
    }

    public void readFileName(BufferedReader br) throws IOException {
        String fileNameRead;
        int currentFile = 0;
        do{
            fileNameRead = br.readLine();
            echoServerModel.getFileNameList().add(fileNameRead);
            currentFile++;
        }while(currentFile!=echoServerModel.getFileCounter());
    }

    public void readFileSize(BufferedReader br) throws IOException{
        int currentFile = 0;
        do{
            String fileSize = br.readLine();
            echoServerModel.getFileSizeList().add(Long.parseLong(fileSize));
            currentFile++;

        }while(currentFile!=echoServerModel.getFileCounter());
    }

    public void saveFile(DataInputStream dis) throws IOException {
        int currentFile = 0;
        byte[] buffer = new byte[4096];
        do{
            FileOutputStream fos = new FileOutputStream(new File(echoServerModel.getUserCatalog() + "\\" + echoServerModel.getFileNameList().get(currentFile)));

            long filesize = echoServerModel.getFileSizeList().get(currentFile); // Send file size in separate msg

            int read = 0;
            //int totalRead = 0;
            long remaining = filesize;
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                System.out.println("buffer.length = " + buffer.length);
                System.out.println("remaining =" + remaining);
                //totalRead += read;
                remaining -= read;
                //System.out.println("read " + totalRead + " bytes.");
                //System.out.println("pozostało " + remaining + " bytes.");
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            currentFile++;
        }while(currentFile!=echoServerModel.getFileCounter());
        dis.close();
        echoServerModel.getFileNameList().clear();
        echoServerModel.setFileCounter(0);
        echoServerModel.getFileSizeList().clear();
    }
}
