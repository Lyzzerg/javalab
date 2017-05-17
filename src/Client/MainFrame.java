package Client;

import javax.swing.*;
import java.awt.*;
import java.io.*;


/**
 * Created by Евгений on 21.03.2017.
 */

public class MainFrame extends JFrame{

    static SendListener sendListener;
    static ReceiveListener receiveListener;

    MainFrame(){
        super("Drawing Application");
        createGUI();
    }

    private void createClearButton(DrawingPane _pane){
        Button clearButton = new Button("Clear");
        clearButton.addActionListener(e -> {
            _pane.Clear();
            _pane.grabFocus();
        });
        getContentPane().add(clearButton);
    }

    public void createGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPane draw = new DrawingPane();
        getContentPane().setLayout(new GridBagLayout());
        draw.setFocusable(true);
        draw.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(draw);
        createClearButton(draw);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        /* client part */
        ServerListener connection = new ServerListener();
        try {
            connection.Connect();
        } catch (Exception e) {
            System.out.println("cannot connect lul");
            System.exit(1);
        }
        sendListener = new SendListener(connection);
        receiveListener = new ReceiveListener(connection);
        ServerHandler serverHandler = new ServerHandler(sendListener,receiveListener);
        serverHandler.start();
        JFrame.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        DrawingPane drawingPane = (DrawingPane)frame.getContentPane().getComponent(0);
        drawingPane.setleftcorner(drawingPane.getLocationOnScreen());
        System.out.println("FrameStarted");
        while(serverHandler.isAlive()){
            Thread.sleep(10);
        }
        System.exit(1);
    }
}
