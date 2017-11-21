package server.controller;

import server.model.EchoServerModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    private int port;
    private boolean sendFirstInfo, sendSecondInfo;
    private ServerSocket serverSocket;
    private InputStream is;
    private BufferedReader br;
    private DataInputStream dis;

    private OutputStream os;
    private PrintWriter pw;
    private DataOutputStream dos;


    private EchoServerModel echoServerModel;
    private ReceiveFiles rf;
    private SendFiles sf;
    //private ExecutorService executorService;

    private boolean serverActive = true;

    Thread thread;

    public EchoServer(int port) {
        this.port = port;
        echoServerModel = new EchoServerModel();
        rf = new ReceiveFiles(echoServerModel);
        sf = new SendFiles(echoServerModel);
        run();
    }

    public void run(){
        Runnable serverTask = () -> {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Create server socket: " + e);
                return;
            }
            while (serverActive){
                try {
                    Socket socket = serverSocket.accept();

                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    dis = new DataInputStream(is);

                    os = socket.getOutputStream();
                    pw = new PrintWriter(os, true);
                    dos = new DataOutputStream(os);

                    sendFirstInfo = rf.readShouldISendFiles(br);
                    sendSecondInfo = rf.readShouldISendFiles(br);

                    if (!sendFirstInfo){
                        if (sendSecondInfo) {
                            serverSendFiles();
                        } else {
                            serverReceiveFiles();
                        }
                    }else{
                        sendFileNamesOnServer();
                    }
                } catch (Exception e) {
                    System.out.println("Server exception: " + e);
                }
            }
        };
        thread = new Thread(serverTask);
        thread.start();
    }

    private void sendFileNamesOnServer() throws IOException{
        sf.sendFileCounter(pw);
        sf.sendFileNames(pw);
        sf.sendFileSize(pw);
    }

    private void serverSendFiles() throws IOException{
        sf.sendFileCounter(pw);
        sf.sendFileNames(pw);
        sf.sendFileSize(pw);
        sf.sendFile(dos);
    }

    private void serverReceiveFiles() throws IOException{
        echoServerModel.setFileCounter(rf.readCounter(br));
        rf.readFileName(br);
        rf.readFileSize(br);
        rf.saveFile(dis);
    }
}
