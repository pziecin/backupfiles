package client.model;

import java.io.*;

/**
 * Created by Piotrek on 2017-10-02.
 */
public class LoginModel {
    private String nick;
    private String password;

    int port;   //SocketModel?

    public LoginModel() {
        this.nick = "";
        this.password = "";
    }

    public void savePort(){
        try{
            PrintWriter out = new PrintWriter("clientinfo.txt" );
            out.println(port);
            out.close();
        }catch(FileNotFoundException fnfe) {
        }
    }

    public boolean getPortFromFile(){
        try{
        File file = new File("clientinfo.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        if(br.readLine() != "0" || br.readLine() != null){
            st = br.readLine();
            port = Integer.parseInt(st);;
            return true;
        }}catch (Exception e){}
        return false;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setNick(String nick){
        this.nick = nick;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getNick(){
        return nick;
    }

    public String getPassword(){
        return password;
    }
}
