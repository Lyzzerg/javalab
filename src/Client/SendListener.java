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

    Object primitive;
    ServerListener connection;
    private boolean working = true;

    SendListener(ServerListener _connection){
        connection = _connection;
        primitive = null;
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
        connection.getSerializer().writeObject(primitive);
        connection.getSerializer().flush();
        System.out.println("sended");
        primitive = null;
    }

    public void  setPrimitive(Object _primitive){
        primitive = _primitive;
        if(primitive instanceof Packet) {
            System.out.println("setting done");
            interrupt();
        } else if(primitive instanceof MovingPrimitive){
            System.out.println("moving done");
            interrupt();
        }
    }

    public void Stop(){
        working = false;
    }
}
