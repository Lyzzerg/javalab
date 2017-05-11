package Server;

import Primitives.Primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import Primitives.*;

/**
 * Created by nesoldr on 27.04.17.
 */
public class ClientListening extends Thread {
    public ObjectInputStream input;
    public ObjectOutputStream output;
    public static ArrayList<Primitives> primitives = new ArrayList<>();

    ClientListening(){
        input = null;
        output = null;
    }

    public void run(){

        System.out.println("Welcome to Server.Server side");

        ServerSocket servers = null;
        Socket fromclient = null;

        // create server socket
        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }
        try {
            System.out.print("Waiting for a client...");
            fromclient= servers.accept();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        }

        System.out.println("Wait for messages");

        try {
            input = new ObjectInputStream(fromclient.getInputStream());
            //output = new ObjectOutputStream(fromclient.getOutputStream());

        } catch (IOException e){
            System.out.println("Client disconnected without sending EXIT message");
        }
        try{
            while(true)
            {
                if(input.available()!=0) {
                    if (input.readObject() instanceof Line) {
                        primitives.add((Line) input.readObject());
                        System.out.println("LINE");
                    }
                    else if (input.readObject() instanceof MyRectangle) {
                        primitives.add((MyRectangle) input.readObject());
                        System.out.println("RECTAGLE");
                    }
                    else if (input.readObject() instanceof Circle) {
                        primitives.add((Line) input.readObject());
                        System.out.println("CIRCLE");
                    }
                    else {
                        System.out.println("niche tam net");
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
