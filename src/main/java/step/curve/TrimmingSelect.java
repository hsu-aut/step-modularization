package step.curve;

import step.ParameterValue;
import step.point.Point;

public class TrimmingSelect{
    private Point cartesianPoint;
    private ParameterValue value;

    public Point getCartesianPoint() {
        return cartesianPoint;
    }

    public void setCartesianPoint(Point cartesianPoint) {
        this.cartesianPoint = cartesianPoint;
    }

    public ParameterValue getValue() {
        return value;
    }

    public void setValue(ParameterValue value) {
        this.value = value;
    }

}
