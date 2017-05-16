package Server;

import java.io.IOException;
import java.util.*;

/**
 * Created by Евгений on 14.05.2017.
 */
public class SendListener extends Thread {
    private ClientListener connection;
    private boolean working = true;
    private volatile Queue<Object> primitive = new LinkedList<>();;

    SendListener(ClientListener _connection){
        connection=_connection;
        System.out.println("Send created");
    }


    public ClientListener getConnection() {
        return connection;
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitive.remove());
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
                        while(!primitive.isEmpty())
                            Send();
                    } catch (IOException e1) {
                        System.out.println("client disconnected");
                        System.out.println("killing send thread");
                        Stop();
                    }
                    primitive=null;
                }
        }
        System.out.println("send thread dead");
    }
    public void setPrimitive(Object _primitive){
        primitive.add(_primitive);
        interrupt();
    }
    public void Stop(){
        working = false;
    }
}
