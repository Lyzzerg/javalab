package Figures;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by nesoldr on 27.04.17.
 */
public class Circle extends Primitives implements Serializable {
    private Point lupperpoint;
    private Point rdownpoint;

    public Circle(Point _lupperpoint, Point _rdownpoint){
        lupperpoint=_lupperpoint;
        rdownpoint=_rdownpoint;
    }
    public Circle(Packet _packet){
        lupperpoint= new Point((int)_packet.getX1(),(int)_packet.getY1());
        rdownpoint=new Point((int)_packet.getX2(),(int)_packet.getY2());
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

    @Override
    public void setFirst(Point _point) {
        lupperpoint = _point;
    }

    @Override
    public void setSecond(Point _point) {
        rdownpoint = _point;
    }

    @Override
    public Point getFirst() {
        return lupperpoint;
    }

    @Override
    public Point getSecond() {
        return rdownpoint;
    }

    @Override
    public Integer numberOfNearestPoint(Point _point) {
        return 1;
    }

    @Override
    public Packet ToPacket() {
        Packet result = new Packet(2, (float)lupperpoint.getX(), (float)lupperpoint.getY(), (float)rdownpoint.getX(),(float)rdownpoint.getY());
        return result;
    }

    private Point getCenter(){
        return new Point((int)(lupperpoint.getX()+(rdownpoint.getX()-lupperpoint.getX())/2),((int)(lupperpoint.getY()+(rdownpoint.getY()-lupperpoint.getY())/2)));
    }
}
