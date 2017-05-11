package Server; /**
 * Created by Евгений on 24.03.2017.
 */
import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClientListening listeningclients = new ClientListening();
        listeningclients.start();
    }
}

