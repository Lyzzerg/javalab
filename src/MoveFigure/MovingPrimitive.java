package MoveFigure;

import Figures.Packet;

import java.io.Serializable;

/**
 * Created by Евгений on 17.05.2017.
 */
public class MovingPrimitive implements Serializable{

    public int getNumberInMassive() {
        return numberInMassive;
    }
    public void setNumberInMassive(int numberInMassive) {
        this.numberInMassive = numberInMassive;
    }
    public int getNumberOfPoint() {
        if(numberOfPoint)
            return 1;
        else
            return 0;
    }
    public void setNumberOfPoint(boolean numberOfPoint) {
        this.numberOfPoint = numberOfPoint;
    }
    public Packet getPrimitive() {
        return primitive;
    }
    public void setPrimitive(Packet primitive) {
        this.primitive = primitive;
    }

    private int numberInMassive;
    private boolean numberOfPoint;
    private Packet primitive;

    public MovingPrimitive(int numberInMassive, boolean numberOfPoint, Packet primitive) {
        this.numberInMassive = numberInMassive;
        this.numberOfPoint = numberOfPoint;
        this.primitive = primitive;
    }
}
