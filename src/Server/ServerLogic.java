package Server; /**
 * Created by Евгений on 24.03.2017.
 */
import Figures.Packet;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerLogic implements Runnable{
    static ServerSocket serverSocket;

    volatile boolean keepProcessing=true;

    volatile static ArrayList<Packet> primitives = new ArrayList<>();
    volatile static ArrayList<Mutex> mutexes = new ArrayList<>();
    volatile static ArrayList<ClientHandler> handlers = new ArrayList<>();
    volatile static int num_added_figure=-1;

    @Override
    public void run() {
        System.out.println("ServerLogic Started\n");
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e){
            e.printStackTrace();
        }
        while(keepProcessing){
            try{
                System.out.println("accepting client\n");
                Socket socket = serverSocket.accept();
                System.out.println("client connected\n");
                process(socket);
            } catch (Exception e){
                handle(e);
            }
        }
    }
    void process(Socket socket) {
        if (socket == null)
            return;
        ClientHandler clientHandler = new ClientHandler(socket);
        handlers.add(clientHandler);
        clientHandler.start();
    }

    public void stopProcessing(){
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }
    private void handle(Exception e){
        if(!(e instanceof SocketException)){
            e.printStackTrace();
        }
    }
    private void closeIgnoringException(Socket socket){
        if(socket!=null) {
            try {
                socket.close();
            } catch (IOException ignore) {
            }
        }
    }
    private void closeIgnoringException(ServerSocket serverSocket){
        if(serverSocket!=null) {
            try {
                serverSocket.close();
            } catch (IOException ignore) {
            }
        }
    }
}

