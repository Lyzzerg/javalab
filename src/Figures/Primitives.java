package Figures;

import java.awt.*;

/**
 * Created by nesoldr on 27.04.17.
 */
public abstract class Primitives {
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
