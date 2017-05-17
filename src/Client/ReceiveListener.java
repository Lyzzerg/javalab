package Client;

import Figures.*;
import MoveFigure.MovingPrimitive;

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
        if(primitive instanceof Packet) {
            if(((Packet) primitive).getType()==1) {
                DrawingPane.primitivesgroup.add(new Line((Packet)primitive));
            }
            else if(((Packet) primitive).getType()==2){
                DrawingPane.primitivesgroup.add(new Circle((Packet)primitive));
            } else if(((Packet) primitive).getType()==3){
                DrawingPane.primitivesgroup.add(new MyRectangle((Packet)primitive));
            }
            DrawingPane.curr++;
        }
        if(primitive instanceof MovingPrimitive){
            System.out.println("двигаем типа лол");
            if(((MovingPrimitive) primitive).getPrimitive().getType() == 1)
                DrawingPane.primitivesgroup.set(((MovingPrimitive) primitive).getNumberInMassive(), new Line(((MovingPrimitive) primitive).getPrimitive()));
            else if(((MovingPrimitive) primitive).getPrimitive().getType() == 2)
                DrawingPane.primitivesgroup.set(((MovingPrimitive) primitive).getNumberInMassive(), new Circle(((MovingPrimitive) primitive).getPrimitive()));
            else if(((MovingPrimitive) primitive).getPrimitive().getType() == 3)
                DrawingPane.primitivesgroup.set(((MovingPrimitive) primitive).getNumberInMassive(), new MyRectangle(((MovingPrimitive) primitive).getPrimitive()));
        }
        primitive = null;
    }

    public void Stop(){
        working = false;
    }
}
