/**
 * Created by Евгений on 24.03.2017.
 */
import java.io.*;
import java.net.*;

class ListeningClientTread extends Thread {
    public void run(){

        System.out.println("Welcome to Server side");

        BufferedReader in = null;
        PrintWriter    out= null;

        ServerSocket servers = null;
        Socket       fromclient = null;

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
        try {
            in  = new BufferedReader(new
                    InputStreamReader(fromclient.getInputStream()));
            out = new PrintWriter(fromclient.getOutputStream(),true);
        } catch (IOException e) {
        }

        String input, output;

        System.out.println("Wait for messages");
        try {
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) break;
                out.println("S ::: " + input);
                System.out.println(input);
            }
        } catch (IOException e){
            System.out.println("Client disconnected without sending EXIT message");
        }
        try {
            out.close();
            in.close();
            fromclient.close();
            servers.close();
        }catch (IOException e){

        }
    }
}

public class Server {

    public static void main(String[] args) throws IOException {
        Thread listeningclients = new ListeningClientTread();
        listeningclients.start();
        Thread doingwtf = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                    }
                    System.out.println("wtf");
                }
            }
        });
        doingwtf.start();
    }
}

