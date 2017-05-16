package Client;

import Figures.Primitives;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Евгений on 14.05.2017.
 */
public class SendListener  extends Thread {

    Object primitive=null;
    ServerListener connection;
    private boolean working = true;

    SendListener(ServerListener _connection){
        connection = _connection;
    }

    @Override
    public void run() {
        while (working){
            try {
                System.out.println("sleeping send");
                Thread.sleep(1000000000);
            } catch (InterruptedException e) {
                try {
                    System.out.println("sending");
                    Send();
                    primitive = null;
                } catch (IOException e1) {
                }
            }

        }
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitive);
        connection.getSerializer().flush();
        primitive=null;
        System.out.println("sended");
    }

    public void  setPrimitive(Object _primitive){
        primitive=_primitive;
        if(primitive instanceof Primitives) {
            System.out.println("setting done");
            interrupt();
        }
    }

    public void Stop(){
        working = false;
    }
}
