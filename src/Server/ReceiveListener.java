package Server;

import Figures.Primitives;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.io.IOException;

/**
 * Created by Евгений on 14.05.2017.
 */
public class ReceiveListener extends Thread {
    private Object primitive=null;
    private ClientListener connection;
    private boolean working = true;

    ReceiveListener(ClientListener _connection){
        connection=_connection;
    }


    @Override
    public void run() {
        while(working) {
            if (primitive == null) {
                try {
                    //System.out.println("trying receive from "+connection.getClient());
                    Receive();
                } catch (IOException e) {
                    System.out.println("client disconnected");
                    System.out.println("killing receive client thread");
                    Stop();
                } catch (ClassNotFoundException e) {
                }
            }
        }
        System.out.println("receive thread dead");
    }

    private void Receive() throws IOException, ClassNotFoundException {
        primitive = connection.getDeserializer().readObject();
        if(primitive instanceof Primitives) {
            ServerLogic.primitives.add((Primitives) primitive);
            while(ServerLogic.primitives.get(ServerLogic.num_added_figure + 1) == null){
                try {
                    System.out.println("not added");
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            System.out.println(ServerLogic.num_added_figure);
            System.out.println("added");
            ServerLogic.mutexes.add(new Mutex());
            ServerLogic.mutexes.add(new Mutex());
            connection.increaseNumOfFigures();
            ServerLogic.num_added_figure++;
            System.out.println(ServerLogic.num_added_figure);
            System.out.println("Element"+primitive+"received");
        } else if(primitive instanceof Integer){
            try {
                Integer numberOfPrimitive = (Integer) primitive;
                System.out.println("changing primitive number " + numberOfPrimitive);
                Integer numberOfPoint = (Integer) connection.getDeserializer().readObject();
                System.out.println("changing point " + numberOfPoint);
                ServerLogic.mutexes.get(numberOfPrimitive*2+numberOfPoint).acquire();
                System.out.println("mutex acquired");
                primitive = connection.getDeserializer().readObject();
                if(numberOfPoint==0) {
                    ServerLogic.primitives.get(numberOfPrimitive).setFirst(((Primitives) primitive).getFirst());
                    System.out.println("first point changed");
                }
                else if(numberOfPoint==1){
                    ServerLogic.primitives.get(numberOfPrimitive).setSecond(((Primitives)primitive).getSecond());
                    System.out.println("second point changed");
                }
                ServerLogic.mutexes.get(numberOfPrimitive*2+numberOfPoint).release();
                System.out.println("mutex released");
            } catch (InterruptedException e) {
            }
        }
        primitive = null;
    }
    public void Stop() {
        working = false;
        try {
            connection.closeConnection();
        } catch (IOException e) {
        }
    }
}
