package Client;

import Primitives.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by nesoldr on 27.04.17.
 */
public class DrawingPane extends JComponent {

    private Point startPoint;
    private Point endPoint;
    private int type = 1;
    private int curr = -1;
    private int numofnearestfigure;
    private boolean moveObject=false;
    private double eps = 20;
    private Point leftcorner;
    public boolean addedflag=false;

    public void setleftcorner(Point _point){
        leftcorner = _point;
    }
    public ArrayList<Primitives> primitivesgroup = new ArrayList<>();

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

    class CustomMouseMotionListener implements MouseMotionListener {

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
                    }else if(primitivesgroup.get(numofnearestfigure) instanceof MyRectangle){
                        MyRectangle rectangle = (MyRectangle) primitivesgroup.get(numofnearestfigure);
                        rectangle.changeNearest(e.getPoint());
                    }
                }else if(!moveObject){
                    if(primitivesgroup.get(curr) instanceof Line){
                        Line line = (Line) primitivesgroup.get(curr);
                        line.setEnd(e.getPoint());
                    }else if(primitivesgroup.get(curr) instanceof Circle){
                        Circle circle = (Circle) primitivesgroup.get(curr);
                        circle.changeCircle(null, e.getPoint());
                    }else if(primitivesgroup.get(curr) instanceof MyRectangle){
                        MyRectangle rectangle = (MyRectangle) primitivesgroup.get(curr);
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

    class CustomMouseListener implements MouseListener {

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
                        primitivesgroup.add(new MyRectangle(startPoint, startPoint));
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
                        MyRectangle rectangle = (MyRectangle) primitivesgroup.get(curr);
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
            addedflag = true;
            System.out.println(addedflag);
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    class CustomKeyListener implements KeyListener {

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
