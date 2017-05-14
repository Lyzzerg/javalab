package Server; /**
 * Created by Евгений on 24.03.2017.
 */
import Figures.Primitives;
import Figures.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerLogic implements Runnable{
    static ServerSocket serverSocket;

    volatile boolean keepProcessing=true;

    volatile static ArrayList<Primitives> primitives = new ArrayList<>();
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
        Runnable clientHandler = new Runnable() {
            @Override
            public void run() {
                ClientListener connection = new ClientListener(socket);
                connection.Connect();
                SendListener sendListener = new SendListener(connection);
                ReceiveListener receiveListener = new ReceiveListener(connection);
                sendListener.start();
                receiveListener.start();
                int figure_num = num_added_figure;
                while(true) {
                        //если количество фигур не совпадает разослать всем.
                        if(figure_num < num_added_figure) {
                            while(figure_num < num_added_figure){
                                figure_num++;
                                sendListener.setPrimitive(primitives.get(figure_num));
                            }
                        }
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

