package Server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Евгений on 14.05.2017.
 */
public class SendListener extends Thread {
    private ClientListener connection;
    private boolean working = true;
    private Object primitive = null;

    SendListener(ClientListener _connection){
        connection=_connection;
    }


    public ClientListener getConnection() {
        return connection;
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitive);
    }

    @Override
    public void run() {
        while(working){
            if(primitive!=null) {
                try {
                    Send();
                    Thread.sleep(10000);
                    primitive = null;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                }
            }
        }
    }
    public void setPrimitive(Object _primitive){
        primitive=_primitive;
    }
    public void Stop(){
        working = false;
    }
}
