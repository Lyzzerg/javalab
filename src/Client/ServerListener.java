package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Евгений on 14.05.2017.
 */
public class ServerListener {
    private static int serverPort = 4444;
    private static String hostAdress = "localhost";
    private static Socket toServerSocket=null;
    private static ObjectOutputStream serializer=null;
    private static ObjectInputStream deserializer=null;

    public int getServerPort(){
        return serverPort;
    }
    public String getHostAdress(){
        return hostAdress;
    }
    public Socket getSocket(){
        return toServerSocket;
    }
    public ObjectInputStream getDeserializer(){
        return deserializer;
    }
    public ObjectOutputStream getSerializer(){
        return serializer;
    }

    public void setServerPort(int _serverPort){
        serverPort = _serverPort;
    }
    public void setHostAdress(String _adress){
        hostAdress = _adress;
    }
    public void Connect(){
        try {

            System.out.println("Trying connect to ServerLogic.ServerLogic " + hostAdress + " with port " + serverPort);
            toServerSocket = new Socket(hostAdress, serverPort);
            System.out.println("Connected");
            serializer = new ObjectOutputStream(toServerSocket.getOutputStream());
            deserializer = new ObjectInputStream(toServerSocket.getInputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
