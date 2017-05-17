package Client;

import Figures.Primitives;

import java.io.IOException;

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
            } catch (IOException e) {
                System.out.println("server disconnected");
                Stop();
            } catch (ClassNotFoundException e) {
            }
        }
        System.out.println("Receive thread dead");
    }

    private void Receive() throws IOException, ClassNotFoundException {
        if(primitive==null)
            primitive=connection.getDeserializer().readObject();
        if(primitive instanceof Primitives) {
            DrawingPane.primitivesgroup.add((Primitives) primitive);
            DrawingPane.curr++;
        }
        primitive = null;
    }

    public void Stop(){
        working = false;
    }
}
