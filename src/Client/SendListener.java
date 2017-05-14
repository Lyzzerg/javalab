package Client;

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
            if(primitive!=null)
                try {
                    Send();
                    Thread.sleep(10000);
                } catch (IOException e) {
                } catch (InterruptedException e) {
                }
            primitive=null;
        }
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitive);
        connection.getSerializer().flush();
        System.out.println("sended");
    }

    public void setPrimitive(Object _primitive){
        primitive=_primitive;
        System.out.println("setting done");
    }

    public void Stop(){
        working = false;
    }
}
