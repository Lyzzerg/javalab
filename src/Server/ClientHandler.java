package Server;

import MoveFigure.MovingPrimitive;

import java.net.Socket;

/**
 * Created by Евгений on 17.05.2017.
 */
public class ClientHandler extends Thread {
    Socket clientSocket;

    ClientHandler(Socket _clientSocket){
        clientSocket=_clientSocket;
    }
    @Override
    public void run() {
        ClientListener connection = new ClientListener(clientSocket);
        connection.Connect();
        SendListener sendListener = new SendListener(connection);
        ReceiveListener receiveListener = new ReceiveListener(connection);
        sendListener.start();
        receiveListener.start();
        System.out.println(ServerLogic.num_added_figure);
        while(connection.isConnected()) {
            //если количество фигур не совпадает разослать всем.
            if(connection.getNumOfFigures() < ServerLogic.num_added_figure) {
                System.out.println("was num of figures on client " + connection.getClient()+" = " + connection.getNumOfFigures());
                while(connection.getNumOfFigures() < ServerLogic.num_added_figure){
                    connection.increaseNumOfFigures();
                    System.out.println("now num of figures on client "+connection.getClient()+
                            " = " + connection.getNumOfFigures());
                    System.out.println(ServerLogic.primitives.get(connection.getNumOfFigures()));
                    while(ServerLogic.primitives.get(connection.getNumOfFigures()) == null){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                        }
                    }
                    sendListener.setPrimitive(ServerLogic.primitives.get(connection.getNumOfFigures()));
                }
            }
        }
       sendListener.Stop();
    }
}
