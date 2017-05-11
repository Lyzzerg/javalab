package Server;

/**
 * Created by Евгений on 11.05.2017.
 */
public class Server {
    public static void main(String[] args){
        Thread server = new Thread(new ServerLogic());
        server.start();
    }
}
