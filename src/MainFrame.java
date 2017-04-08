import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;


/**
 * Created by Евгений on 21.03.2017.
 */

class Client implements Runnable{
    @Override
    public void run() {
        Socket ToServerSocket = null;

        int ServerPort = 4444;
        String HostAdress = "localhost";

        try {
            System.out.println("Trying connect to Server " + HostAdress + " with port " + ServerPort);
            ToServerSocket = new Socket(HostAdress, ServerPort);
            System.out.println("Connected");
            BufferedReader in = new
                    BufferedReader(new
                    InputStreamReader(ToServerSocket.getInputStream()));
            PrintWriter out = new
                    PrintWriter(ToServerSocket.getOutputStream(), true);
            BufferedReader inu = new
                    BufferedReader(new InputStreamReader(System.in));


            ObjectOutputStream serializer = new ObjectOutputStream(ToServerSocket.getOutputStream());
            ObjectInputStream deserializer = new ObjectInputStream(ToServerSocket.getInputStream());

            String Fileuser, FileServer;

            while ((Fileuser = inu.readLine()) != null) {
                out.println(Fileuser);
                FileServer = in.readLine();
                System.out.println(FileServer);
                if (Fileuser.equalsIgnoreCase("close")) break;
                if (Fileuser.equalsIgnoreCase("exit")) break;
            }

            out.close();
            in.close();
            inu.close();
            ToServerSocket.close();
        } catch(IOException e) {
            System.out.println("Can't connect to server localhost with port 4444");
        }
    }
}

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

    public static void main(String args[]) throws IOException {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainFrame frame = new MainFrame();
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                DrawingPane drawingPane = (DrawingPane)frame.getContentPane().getComponent(0);
                drawingPane.setleftcorner(drawingPane.getLocationOnScreen());
            }
        });
        System.out.println("FrameStarted");
        /* client part */
        Thread client = new Thread(new Client());
        client.setDaemon(true);
        client.start();
    }
}

class DrawingPane extends JComponent {

    private Point startPoint;
    private Point endPoint;
    private int type = 1;
    private int curr = -1;
    private int numofnearestfigure;
    private boolean moveObject=false;
    private double eps = 20;
    private Point leftcorner;

    public void setleftcorner(Point _point){
        leftcorner = _point;
    }
    private ArrayList<Primitives> primitivesgroup = new ArrayList<>();

    abstract class Primitives {
        protected boolean moving=false;
        public abstract void paint(Graphics gr);
        public abstract double distancetoNearestPoint(Point _point);
        public void enableMoving(){
            moving=true;
        }
        public void disableMoving(){
            moving=false;
        }
    }

    class Line extends Primitives implements Serializable{
        private Point start;
        private Point end;

        Line(Point _start, Point _end){
            start=_start;
            end=_end;
        }

        Line(double x1, double y1, double x2, double y2){
            start.setLocation(x1,y1);
            end.setLocation(x2,y2);
        }

        public Point getStart(){
            return start;
        }
        public Point getEnd(){
            return end;
        }
        private void setStart(Point _start){
            start=_start;
        }
        private void setEnd(Point _end){
            end=_end;
        }
        public void changeLine(Point _start, Point _end){
            if(_start!=null){
                setStart(_start);
            }
            if(_end!=null) {
                setEnd(_end);
            }
        }
        @Override
        public void paint(Graphics gr){
            if(moving) {
                gr.setColor(Color.red);
            }
            else{
                gr.setColor(Color.black);
            }
            gr.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
        }
        @Override
        public double distancetoNearestPoint(Point _point){
            return Math.min(Math.sqrt(Math.pow(_point.getX()-start.getX(),2)+Math.pow(_point.getY()-start.getY(),2)), //расстояние до начала линии
                    Math.sqrt(Math.pow(_point.getX()-end.getX(),2)+Math.pow(_point.getY()-end.getY(),2))); //расстояние до конца линии

        }
        public void changeNearestPoint(Point _point){
            if(Math.sqrt(Math.pow(_point.getX()-start.getX(),2)+Math.pow(_point.getY()-start.getY(),2))
                    < Math.sqrt(Math.pow(_point.getX()-end.getX(),2)+Math.pow(_point.getY()-end.getY(),2))){
                setStart(_point);
            }else{
                setEnd(_point);
            }
        }
    }

    class Circle extends Primitives implements Serializable{
        private Point lupperpoint;
        private Point rdownpoint;

        Circle(Point _lupperpoint, Point _rdownpoint){
            lupperpoint=_lupperpoint;
            rdownpoint=_rdownpoint;
        }

        public Point getlUpperpoint(){
            return lupperpoint;
        }
        public Point getrDownpoint(){
            return rdownpoint;
        }

        public void changeCircle(Point _lupperpoint, Point _rdownpoint){
            if(_lupperpoint!=null){
                lupperpoint=_lupperpoint;
            }
            if(_rdownpoint!=null) {
                rdownpoint=_rdownpoint;
            }
        }

        @Override
        public void paint(Graphics gr) {
            if(moving) {
                gr.setColor(Color.red);
            }
            else{
                gr.setColor(Color.black);
            }
            if((lupperpoint.getX()<=rdownpoint.getX())&&(lupperpoint.getY()<=rdownpoint.getY()))
                gr.drawOval((int)lupperpoint.getX(),(int)lupperpoint.getY(),Math.abs((int)(rdownpoint.getX()-lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()-lupperpoint.getY())));
            else if((lupperpoint.getX()>=rdownpoint.getX())&&(lupperpoint.getY()<=rdownpoint.getY())){
                gr.drawOval((int)rdownpoint.getX(),(int)lupperpoint.getY(),Math.abs((int)(rdownpoint.getX()-lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()-lupperpoint.getY())));
            }
            else if((lupperpoint.getX()<=rdownpoint.getX())&&(lupperpoint.getY()>=rdownpoint.getY())){
                gr.drawOval((int)lupperpoint.getX(),(int)rdownpoint.getY(),Math.abs((int)(rdownpoint.getX()-lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()-lupperpoint.getY())));
            }
            else if((lupperpoint.getX()>=rdownpoint.getX())&&(lupperpoint.getY()>=rdownpoint.getY())){
                gr.drawOval((int)rdownpoint.getX(),(int)rdownpoint.getY(),Math.abs((int)(rdownpoint.getX()-lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()-lupperpoint.getY())));
            }
            gr.drawOval((int)this.getCenter().getX(),(int)this.getCenter().getY(),2,2);
        }

        @Override
        public double distancetoNearestPoint(Point _point){
            Point circleCenter = this.getCenter();
            return Math.sqrt(Math.pow(_point.getX()-circleCenter.getX(),2)+Math.pow(_point.getY()-circleCenter.getY(),2)); //расстояние до центра круга
        }

        private Point getCenter(){
            return new Point((int)(lupperpoint.getX()+(rdownpoint.getX()-lupperpoint.getX())/2),((int)(lupperpoint.getY()+(rdownpoint.getY()-lupperpoint.getY())/2)));
        }
    }

    class Rectangle extends Primitives implements Serializable{
        private Point lupperpoint;
        private Point rdownpoint;
        Rectangle(Point _upperpoint, Point _downpoint){
            lupperpoint =_upperpoint;
            rdownpoint =_downpoint;
        }
        public Point getLupperpoint(){
            return lupperpoint;
        }
        public Point getRdownpoint(){
            return rdownpoint;
        }

        public void changeRectangle(Point _lupperpoint, Point _rdownpoint){
            if(_lupperpoint!=null){
                lupperpoint =_lupperpoint;
            }
            if(_rdownpoint!=null) {
                rdownpoint =_rdownpoint;
            }
        }

        public void paint(Graphics gr){
            if(moving) {
                gr.setColor(Color.red);
            }
            else{
                gr.setColor(Color.black);
            }
            if((lupperpoint.getX()<= rdownpoint.getX())&&(lupperpoint.getY()<= rdownpoint.getY()))
                gr.drawRect((int) lupperpoint.getX(),(int) lupperpoint.getY(),Math.abs((int)(rdownpoint.getX()- lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()- lupperpoint.getY())));
            else if((lupperpoint.getX()>= rdownpoint.getX())&&(lupperpoint.getY()<= rdownpoint.getY())){
                gr.drawRect((int) rdownpoint.getX(),(int) lupperpoint.getY(),Math.abs((int)(rdownpoint.getX()- lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()- lupperpoint.getY())));
            }
            else if((lupperpoint.getX()<= rdownpoint.getX())&&(lupperpoint.getY()>= rdownpoint.getY())){
                gr.drawRect((int) lupperpoint.getX(),(int) rdownpoint.getY(),Math.abs((int)(rdownpoint.getX()- lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()- lupperpoint.getY())));
            }
            else if((lupperpoint.getX()>= rdownpoint.getX())&&(lupperpoint.getY()>= rdownpoint.getY())){
                gr.drawRect((int) rdownpoint.getX(),(int) rdownpoint.getY(),Math.abs((int)(rdownpoint.getX()- lupperpoint.getX())),Math.abs((int)(rdownpoint.getY()- lupperpoint.getY())));
            }
        }

        public double distancetoNearestPoint(Point _point){
           return Math.min(Math.sqrt(Math.pow(_point.getX()- lupperpoint.getX(),2)+Math.pow(_point.getY()- lupperpoint.getY(),2)), //расстояние до верхней точки
                    Math.sqrt(Math.pow(_point.getX()- rdownpoint.getX(),2)+Math.pow(_point.getY()- rdownpoint.getY(),2))); //расстояние до нижней точки
        }
        public void changeNearest(Point _point){
            if(Math.sqrt(Math.pow(_point.getX()- lupperpoint.getX(),2)+Math.pow(_point.getY()- lupperpoint.getY(),2)) < //расстояние до верхней точки
                    Math.sqrt(Math.pow(_point.getX()- rdownpoint.getX(),2)+Math.pow(_point.getY()- rdownpoint.getY(),2))){//расстояние до нижней точки
                changeRectangle(_point,null);
            }else{
                changeRectangle(null,_point);
            }
        }
    }

    DrawingPane(){
        addMouseListener(new CustomMouseListener());
        addKeyListener(new CustomKeyListener());
        addMouseMotionListener(new CustomMouseMotionListener());
    }

    @Override
    protected void paintComponent(Graphics gr){
        if(!primitivesgroup.isEmpty())
            for (Primitives p:primitivesgroup) {
            p.paint(gr);
        }
    }

    public Dimension getPreferredSize(){
        return new Dimension(800,600);
    }

    public void Clear(){
        primitivesgroup.clear();
        curr=-1;
        repaint();
    }

    class CustomMouseMotionListener implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            if(!primitivesgroup.isEmpty()) {
                if(moveObject&&(numofnearestfigure!=-1)){
                    if(primitivesgroup.get(numofnearestfigure) instanceof Line){
                        Line line = (Line) primitivesgroup.get(numofnearestfigure);
                        line.changeNearestPoint(e.getPoint());
                    }else if(primitivesgroup.get(numofnearestfigure) instanceof Circle){
                        Circle circle = (Circle) primitivesgroup.get(numofnearestfigure);
                        circle.changeCircle(null, e.getPoint());
                    }else if(primitivesgroup.get(numofnearestfigure) instanceof Rectangle){
                        Rectangle rectangle = (Rectangle) primitivesgroup.get(numofnearestfigure);
                        rectangle.changeNearest(e.getPoint());
                    }
                }else if(!moveObject){
                    if(primitivesgroup.get(curr) instanceof Line){
                        Line line = (Line) primitivesgroup.get(curr);
                        line.setEnd(e.getPoint());
                    }else if(primitivesgroup.get(curr) instanceof Circle){
                        Circle circle = (Circle) primitivesgroup.get(curr);
                        circle.changeCircle(null, e.getPoint());
                    }else if(primitivesgroup.get(curr) instanceof Rectangle){
                        Rectangle rectangle = (Rectangle) primitivesgroup.get(curr);
                        rectangle.changeRectangle(null, e.getPoint());
                    }
                }
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    class CustomMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("MouseClicked");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            System.out.println("mouse pressed");
            if(!moveObject) {
                switch (type) {
                    case 1:
                        primitivesgroup.add(new Line(startPoint, startPoint));
                        break;
                    case 2:
                        primitivesgroup.add(new Circle(startPoint, startPoint));
                        break;
                    case 3:
                        primitivesgroup.add(new Rectangle(startPoint, startPoint));
                        break;
                    default:
                        break;
                }
                curr++;
            }
            else{
                int counts=0;
                double distance=1000;
                for (Primitives p:primitivesgroup) {
                    if(p.distancetoNearestPoint(startPoint)<distance)
                    {
                        distance=p.distancetoNearestPoint(startPoint);
                        numofnearestfigure=counts;
                    }
                    counts++;
                }
                if(distance>eps){
                    numofnearestfigure=-1;
                }else{
                    primitivesgroup.get(numofnearestfigure).enableMoving();
                }
                if(numofnearestfigure!=-1)
                    if(primitivesgroup.get(numofnearestfigure) instanceof Circle){
                        Circle circle =(Circle) primitivesgroup.get(numofnearestfigure);
                        System.out.println("X was "+(int)circle.getrDownpoint().getX()+"Y was "+(int)circle.getrDownpoint().getY());
                        try{
                            Robot r = new Robot();
                            r.mouseMove((int)(leftcorner.getX()+circle.getrDownpoint().getX()),(int)(leftcorner.getY()+circle.getrDownpoint().getY()));
                            System.out.println((int)leftcorner.getX()+" "+(int)leftcorner.getY());
                            System.out.println("X now "+(int)(circle.getlUpperpoint().getX())+"Y now"+(int)(circle.getrDownpoint().getY()));
                        }catch (AWTException except){
                        }
                    }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            endPoint = e.getPoint();
            System.out.println("mouse released");
            switch (type) {
                case 1:
                    if (!moveObject) {
                        Line line = (Line) primitivesgroup.get(curr);
                        line.setEnd(e.getPoint());
                        primitivesgroup.set(curr, line);
                    } else {
                        if(numofnearestfigure!=-1)
                         primitivesgroup.get(numofnearestfigure).disableMoving();
                    }
                    break;
                case 2:
                    if(!moveObject) {
                        Circle circle = (Circle) primitivesgroup.get(curr);
                        circle.changeCircle(null, e.getPoint());
                        primitivesgroup.set(curr, circle);
                    }
                    else{
                        if(numofnearestfigure!=-1)
                            primitivesgroup.get(numofnearestfigure).disableMoving();
                    }
                    break;
                case 3:
                    if(!moveObject) {
                        Rectangle rectangle = (Rectangle) primitivesgroup.get(curr);
                        rectangle.changeRectangle(null, e.getPoint());
                        primitivesgroup.set(curr, rectangle);
                    }
                    else{
                        if(numofnearestfigure!=-1)
                            primitivesgroup.get(numofnearestfigure).disableMoving();
                    }
                    break;
                default:
                    break;
            }
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    class CustomKeyListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_1:
                    type =1;
                    System.out.println("type is Line");
                    break;
                case KeyEvent.VK_2:
                    type =2;
                    System.out.println("type is Circle");
                    break;
                case KeyEvent.VK_3:
                    type =3;
                    System.out.println("type is Rectangle");
                    break;
                default:
                    System.out.println("wtf is"+e.getKeyCode());
            }
            if(e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                if(!primitivesgroup.isEmpty())
                {
                    primitivesgroup.remove(primitivesgroup.size() - 1);
                    curr--;
                }
                repaint();
            }
            if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                moveObject=!moveObject;
                if(moveObject)
                    System.out.println("ObjectMovingEnabled");
                else
                    System.out.println("ObjectMovingDisabled");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
