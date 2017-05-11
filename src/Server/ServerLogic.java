package Server; /**
 * Created by Евгений on 24.03.2017.
 */
import Figures.Primitives;
import Figures.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerLogic implements Runnable{
    static ServerSocket serverSocket;
    volatile boolean keepProcessing=true;
    ObjectInputStream input;
    ObjectOutputStream output;
    ArrayList<Primitives> primitives = new ArrayList<>();

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
        Runnable clientHandler = new Runnable() {
            @Override
            public void run() {
                System.out.println("ServerLogic: getting message from client"+socket);
                try {
                    System.out.println("receiving input stream from client");
                    input = new ObjectInputStream(socket.getInputStream());
                    //output = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("input stream received");
                    System.out.println(input);
                } catch (IOException e) {
                    System.out.println("Client disconnected without sending EXIT message");
                }
                try {
                    if (input.available() != 0) {
                        if (input.readObject() instanceof Figures.Line) {
                            primitives.add((Figures.Line) input.readObject());
                        } else if (input.readObject() instanceof Figures.MyRectangle) {
                            primitives.add((Figures.MyRectangle) input.readObject());
                            System.out.println("RECTAGLE");
                        } else if (input.readObject() instanceof Figures.Circle) {
                            primitives.add((Figures.Line) input.readObject());
                            System.out.println("CIRCLE");
                        } else {
                            System.out.println("niche tam net");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread clientConnection = new Thread(clientHandler);
        clientConnection.start();
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

