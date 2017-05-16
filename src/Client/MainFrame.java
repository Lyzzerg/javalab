package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import Figures.*;


/**
 * Created by Евгений on 21.03.2017.
 */

public class MainFrame extends JFrame{

    MainFrame(SendListener _sendListener){
        super("Drawing Application");
        createGUI(_sendListener);
    }

    private void createClearButton(DrawingPane _pane){
        Button clearButton = new Button("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _pane.Clear();
                _pane.grabFocus();
            }
        });
        getContentPane().add(clearButton);
    }

    public void createGUI(SendListener _sendListener){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPane draw = new DrawingPane(_sendListener);
        getContentPane().setLayout(new GridBagLayout());
        draw.setFocusable(true);
        draw.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(draw);
        createClearButton(draw);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        /* client part */
        ServerListener connection = new ServerListener();
        connection.Connect();
        SendListener sending = new SendListener(connection);
        ReceiveListener receiving = new ReceiveListener(connection);
        sending.start();
        receiving.start();
        JFrame.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame(sending);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        DrawingPane drawingPane = (DrawingPane)frame.getContentPane().getComponent(0);
        drawingPane.setleftcorner(drawingPane.getLocationOnScreen());
        System.out.println("FrameStarted");
    }
}
