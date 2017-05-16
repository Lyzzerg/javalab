package Server;

import Figures.Primitives;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Евгений on 14.05.2017.
 */
public class ReceiveListener extends Thread {
    private Object primitive=null;
    private ClientListener connection;
    private boolean working = true;

    ReceiveListener(ClientListener _connection){
        connection=_connection;
    }


    @Override
    public void run() {
        while(working) {
            if (primitive == null) {
                try {
                    System.out.println("trying receive from "+connection.getClient());
                    Receive();
                } catch (IOException e) {
                    System.out.println("client disconnected");
                    System.out.println("killing receive client thread");
                    Stop();
                } catch (ClassNotFoundException e) {
                }
            }
        }
        System.out.println("receive thread dead");
    }

    private void Receive() throws IOException, ClassNotFoundException {
        primitive = connection.getDeserializer().readObject();
        if(primitive instanceof Primitives) {
            ServerLogic.primitives.add((Primitives) primitive);
            ServerLogic.num_added_figure++;
            System.out.println("Element"+primitive+"received");
        }
        primitive = null;
    }
    public void Stop() {
        working = false;
        try {
            connection.closeConnection();
        } catch (IOException e) {
        }
    }
}
