package Server;

import java.io.IOException;
import java.util.*;

/**
 * Created by Евгений on 14.05.2017.
 */
public class SendListener extends Thread {
    private ClientListener connection;
    private boolean working = true;
    private Queue<Object> primitives;

    SendListener(ClientListener _connection){
        connection=_connection;
        System.out.println("Send created");
        primitives = new LinkedList<>();
    }


    public ClientListener getConnection() {
        return connection;
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitives.remove());
        connection.getSerializer().flush();
    }

    @Override
    public void run() {
        while(connection.isConnected()){
                try {
                    System.out.println("sleeping send");
                    Thread.sleep(1000000000);
                } catch (InterruptedException e) {
                    try {
                        while(!primitives.isEmpty())
                            Send();
                    } catch (IOException e1) {
                        System.out.println("client disconnected");
                        System.out.println("killing send thread");
                        Stop();
                    }
                }
        }
        System.out.println("send thread dead");
    }
    public void setPrimitive(Object _primitive){
        if(_primitive!=null) {
            primitives.add(_primitive);
            interrupt();
        }
    }
    public void Stop(){
        working = false;
    }
}
