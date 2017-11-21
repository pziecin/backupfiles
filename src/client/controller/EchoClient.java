package client.controller;


import client.model.EchoClientModel;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class EchoClient {

    private Socket socket;
    OutputStream os;
    PrintWriter pw;
    InputStream is;
    BufferedReader br;

    private EchoClientModel echoClientModel;

    public EchoClient(){
        echoClientModel = new EchoClientModel();
    }

    public void setSocket(int port) throws IOException {
        this.socket = new Socket("localhost", port);
    }

    public void sendYouShouldSendFiles(boolean sendFile) throws IOException{
        os = socket.getOutputStream();
        pw = new PrintWriter(os, true);
        pw.println(sendFile);
    }

    public void sendFileCounter(int fileCounter) throws IOException{
        os = socket.getOutputStream();
        pw = new PrintWriter(os, true);
        pw.println(fileCounter);
    }

    public void sendFileNames(String filename) throws IOException {
            os = socket.getOutputStream();
            pw = new PrintWriter(os, true);
            pw.println(filename);
    }

    public void sendFileSize(Long filesize) throws IOException {
        os = socket.getOutputStream();
        pw = new PrintWriter(os, true);
        pw.println(filesize);

    }

    public void sendFile(String file, Long filesize, ProgressBar pbc) throws IOException{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];

        int read = 0;
        int totalRead = 0;
        long remaining = filesize;
        while((read = fis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
            remaining -= read;
            totalRead += read;
            pbc.setProgress(totalRead/filesize);   // do poprawy
            dos.write(buffer, 0, read);
        }

        dos.flush();

        fis.close();
    }


    public void readCounter() throws IOException {
        is = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        int counter = Integer.parseInt(br.readLine());
        echoClientModel.setFileCounter(counter);
    }

    public int returnCounter(){
        return echoClientModel.getFileCounter();
    }

    public List<String> getFileNameList() {
        return echoClientModel.getFileNameList();
    }

    public void readFileName() throws IOException {
        is = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        String fileNameRead;
        int currentFile = 0;
        do{
            fileNameRead = br.readLine();
            echoClientModel.getFileNameList().add(fileNameRead);
            currentFile++;
        }while(currentFile!=echoClientModel.getFileCounter());
    }

    public void readFileSize() throws IOException{
        is = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(is));
        int currentFile = 0;
        do{
            String fileSize = br.readLine();
            echoClientModel.getFileSizeList().add(Long.parseLong(fileSize));
            currentFile++;

        }while(currentFile!=echoClientModel.getFileCounter());
    }

    public void saveFile(ProgressBar pbs) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int currentFile = 0;
        byte[] buffer = new byte[4096];

        do{
            FileOutputStream fos = new FileOutputStream(new File(echoClientModel.getUserCatalog() + "\\" + echoClientModel.getFileNameList().get(currentFile)));

            long filesize = echoClientModel.getFileSizeList().get(currentFile); // Send file size in separate msg

            int read = 0;
            double totalRead = 0;
            long remaining = filesize;
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                totalRead += read;
                pbs.setProgress(totalRead/filesize);   // do poprawy
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            currentFile++;
        }while(currentFile!=echoClientModel.getFileCounter());
        dis.close();
        echoClientModel.getFileNameList().clear();
        echoClientModel.setFileCounter(0);
        echoClientModel.getFileSizeList().clear();
    }


    public void closeSocket() throws IOException{
        socket.close();
    }
}
