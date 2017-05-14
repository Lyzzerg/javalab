package Client;

import Figures.Primitives;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Евгений on 14.05.2017.
 */
public class ReceiveListener extends Thread {

    Object primitive=null;
    private boolean working = true;
    ServerListener connection;

    ReceiveListener(ServerListener _connection){
        connection = _connection;
    }

    @Override
    public void run() {
        while (working){
            try {
                Receive();
                Thread.sleep(10000);
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void Receive() throws IOException, ClassNotFoundException {
        if(primitive==null)
            primitive=connection.getDeserializer().readObject();
        if(primitive instanceof Primitives)
            DrawingPane.primitivesgroup.add((Primitives)primitive);
        primitive = null;
    }

    public void Stop(){
        working = false;
    }
}
