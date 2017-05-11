package Client;

import Figures.Primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by nesoldr on 27.04.17.
 */
public class MyClient extends Thread implements Runnable{
    public ObjectOutputStream serializer=null;
    public ObjectInputStream deserializer=null;
    public Socket ToServerSocket=null;
    public final int ServerPort = 4444;
    public final String HostAdress = "localhost";

    @Override
    public void run() {

        try {
            System.out.println("Trying connect to ServerLogic.ServerLogic " + HostAdress + " with port " + ServerPort);
            ToServerSocket = new Socket(HostAdress, ServerPort);
            System.out.println("Connected");
            serializer = new ObjectOutputStream(ToServerSocket.getOutputStream());
            // deserializer = new ObjectInputStream(ToServerSocket.getInputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void Send(Primitives primitive) throws IOException {
        serializer.writeObject(primitive);
        serializer.flush();
        serializer.close();
    }
    public void Recieve(){

    }
}