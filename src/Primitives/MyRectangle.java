package Primitives;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by nesoldr on 27.04.17.
 */
public class MyRectangle extends Primitives implements Serializable {
    private Point lupperpoint;
    private Point rdownpoint;

    public MyRectangle(Point _upperpoint, Point _downpoint){
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
