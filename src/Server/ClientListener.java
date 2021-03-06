package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Евгений on 14.05.2017.
 */
public class ClientListener {
    private Socket client;
    private ObjectOutputStream serializer=null;
    private ObjectInputStream deserializer=null;
    private int numOfFigures = -1;

    ClientListener(Socket _acceptedSocket){
        client = _acceptedSocket;
    }
    public void Connect(){
        try {
            serializer = new ObjectOutputStream(client.getOutputStream());
            deserializer = new ObjectInputStream(client.getInputStream());
            System.out.println(serializer);
            System.out.println(deserializer);
        } catch (IOException e) {
        }
    }
    public Socket getClient(){
        return client;
    }

    public void increaseNumOfFigures(){
        numOfFigures++;
    }
    public int getNumOfFigures(){ return numOfFigures; }
    public boolean isConnected(){
        try {
            client.getOutputStream();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void closeConnection() throws IOException {
        serializer = null;
        deserializer = null;
        client.close();
    }

    public ObjectOutputStream getSerializer() {
        return serializer;
    }

    public ObjectInputStream getDeserializer() {
        return deserializer;
    }
}
