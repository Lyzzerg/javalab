package Client;

import Figures.Primitives;

import java.io.IOException;

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
                    Send();
                    primitive = null;
                } catch (IOException e1) {
                }
            }
        }
        System.out.println("Send thread dead");
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

    public void sendChangeStatement(Integer _primitivenumber, Integer _primitivePoint, Object _primitive){
        System.out.println("sending _primitivenumber");
        primitive = _primitivenumber;
        interrupt();
        System.out.println("sending _primitivePoint");
        primitive = _primitivePoint;
        interrupt();
        System.out.println("sending _primitive");
        primitive = _primitive;
        interrupt();
    }

    public void Stop(){
        working = false;
    }
}
