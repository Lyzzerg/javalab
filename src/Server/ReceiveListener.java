package Server;

import Figures.Packet;
import MoveFigure.MovingPrimitive;
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
        System.out.println("пытаюсь принять");
        primitive = connection.getDeserializer().readObject();
        System.out.println(primitive);
        if(primitive instanceof MovingPrimitive){
            try {
                ServerLogic.mutexes.get(((MovingPrimitive) primitive).getNumberInMassive()*2 +
                        ((MovingPrimitive) primitive).getNumberOfPoint()).acquire();
                System.out.println("mutex blocked");
            } catch (InterruptedException e) {
            }

            primitive = connection.getDeserializer().readObject();
            ServerLogic.primitives.set(((MovingPrimitive) primitive).getNumberInMassive(),((MovingPrimitive) primitive).getPrimitive());
            System.out.println("primitive changed");
            for (ClientHandler h:
                    ServerLogic.handlers) {
                h.getSendListener().setPrimitive(new MovingPrimitive(((MovingPrimitive) primitive).getNumberInMassive(),
                        true, ServerLogic.primitives.get(((MovingPrimitive) primitive).getNumberInMassive())));
                System.out.println("рассылка всем");
            }
            ServerLogic.mutexes.get(((MovingPrimitive) primitive).getNumberInMassive()*2 +
                        ((MovingPrimitive) primitive).getNumberOfPoint()).release();
            System.out.println("мьютекс освобождён");
        }
        if(primitive instanceof Packet) {
            ServerLogic.primitives.add((Packet) primitive);
            System.out.println(ServerLogic.num_added_figure);
            System.out.println("added");
            ServerLogic.mutexes.add(new Mutex());
            ServerLogic.mutexes.add(new Mutex());
            connection.increaseNumOfFigures();
            ServerLogic.num_added_figure++;
            System.out.println(ServerLogic.num_added_figure);
            System.out.println("Element"+primitive+"received");
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
