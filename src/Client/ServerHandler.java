package Client;


/**
 * Created by Евгений on 17.05.2017.
 */
public class ServerHandler extends Thread {

    private SendListener sendListener;
    private ReceiveListener receiveListener;

    ServerHandler(SendListener _sendListener, ReceiveListener _receiveListener){
        sendListener = _sendListener;
        receiveListener = _receiveListener;
    }

    @Override
    public void run() {
        sendListener.start();
        receiveListener.start();
        while(receiveListener.isAlive()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        sendListener.Stop();
        sendListener.interrupt();
    }
}
