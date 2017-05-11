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
