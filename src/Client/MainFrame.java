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

    MainFrame(){
        super("Drawing Application");
        createGUI();
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

    public void createGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPane draw = new DrawingPane();
        getContentPane().setLayout(new GridBagLayout());
        draw.setFocusable(true);
        draw.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(draw);
        createClearButton(draw);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        JFrame.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        DrawingPane drawingPane = (DrawingPane)frame.getContentPane().getComponent(0);
        drawingPane.setleftcorner(drawingPane.getLocationOnScreen());
        System.out.println("FrameStarted");

        /* client part */
        ServerListener connection = new ServerListener();
        connection.Connect();
        SendListener sending = new SendListener(connection);
        ReceiveListener receiving = new ReceiveListener(connection);
        sending.start();
        receiving.start();
        int i=0;
        while(true){
            if(i%1e+8 == 0)
                System.out.println(drawingPane.addedflag);
            if(drawingPane.addedflag){
                //отправить добавленный элемент
                System.out.println("I'M HERE");
                sending.setPrimitive(drawingPane.primitivesgroup.get(drawingPane.primitivesgroup.size()-1));
                drawingPane.addedflag = false;
                System.out.println(drawingPane.addedflag);
            } else{
                //проверить есть ли новые элементы:
                //принять все элементы от сервера
                //сравнивая их с теми, что уже есть
            }
            i = i+1;
        }
    }
}
