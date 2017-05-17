package Figures;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by nesoldr on 27.04.17.
 */
public class Line extends Primitives implements Serializable {
        private Point start;
        private Point end;

        public Line(Point _start, Point _end){
            start=_start;
            end=_end;
        }

        public Line(double x1, double y1, double x2, double y2){
            start.setLocation(x1,y1);
            end.setLocation(x2,y2);
        }

        public Line(Line line){
            start = line.getStart();
            end = line.getEnd();
        }

        public Point getStart(){
            return start;
        }
        public Point getEnd(){
            return end;
        }
        public void setStart(Point _start){
            start=_start;
        }
        public void setEnd(Point _end){
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

    @Override
    public void setFirst(Point _point) {
        start= _point;
    }

    @Override
    public void setSecond(Point _point) {
            end = _point;
    }

    @Override
    public Point getFirst() {
        return start;
    }

    @Override
    public Point getSecond() {
        return end;
    }

    @Override
    public Integer numberOfNearestPoint(Point _point) {
            if(Math.sqrt(Math.pow(_point.getX()-start.getX(),2)+Math.pow(_point.getY()-start.getY(),2))< //расстояние до начала линии
        Math.sqrt(Math.pow(_point.getX()-end.getX(),2)+Math.pow(_point.getY()-end.getY(),2)))
                return 0;
            else
                return 1;
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
