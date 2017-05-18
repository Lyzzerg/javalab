package Client;

import Figures.Packet;
import Figures.Primitives;
import MoveFigure.MovingPrimitive;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Евгений on 14.05.2017.
 */
public class SendListener  extends Thread {

    Queue<Object> primitives;
    ServerListener connection;
    private boolean working = true;

    SendListener(ServerListener _connection){
        connection = _connection;
        primitives = new LinkedList<>();
    }

    @Override
    public void run() {
        while (working){
            try {
                System.out.println("sleeping send");
                Thread.sleep(1000000000);
            } catch (InterruptedException e) {
                try {
                    Send();
                } catch (IOException e1) {
                }
            }
        }
        System.out.println("Send thread dead");
    }

    private void Send() throws IOException {
        connection.getSerializer().writeObject(primitives.remove());
        connection.getSerializer().flush();
        System.out.println("sended");
    }

    public void  setPrimitive(Object _primitive){
        primitives.add(_primitive);
        if(_primitive instanceof Packet) {
            System.out.println("setting");
        } else if(_primitive instanceof MovingPrimitive){
            System.out.println("moving");
        }
        interrupt();
    }

    public void Stop(){
        working = false;
    }
}
