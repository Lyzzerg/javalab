package Client;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Created by Евгений on 17.05.2017.
 */
public class Question extends JFrame {

    Question(String _question){
        super(_question);
        createGUI();
    }
    public void createGUI(){
        //setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Button buttonYes = new Button("Yes");
        buttonYes.addActionListener(e -> {
            ServerListener.answer=true;
            setVisible(false);
        });
        Button buttonNo = new Button("No");
        buttonNo.addActionListener(e -> {
            ServerListener.answer=false;
            setVisible(false);
        });
        getContentPane().setLayout(new GridBagLayout());
        this.setLocation((int)(getDefaultToolkit().getScreenSize().getWidth()/2 - this.getWidth()/2),
                (int)(getDefaultToolkit().getScreenSize().getHeight()/2) - this.getHeight()/2);
        this.setPreferredSize(new Dimension(500,80));
        getContentPane().add(buttonYes);
        getContentPane().add(buttonNo);
        pack();
        setVisible(true);
    }
}
