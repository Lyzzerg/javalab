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
                    Thread.sleep(10000);
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Receive() throws IOException, ClassNotFoundException {
        System.out.println("lil");
        primitive = connection.getDeserializer().readObject();
        System.out.println("lul");
        if(primitive instanceof Primitives) {
            ServerLogic.primitives.add((Primitives) primitive);
            ServerLogic.num_added_figure++;
            System.out.println("Element"+primitive+"received");
        }
        primitive = null;
    }
}
