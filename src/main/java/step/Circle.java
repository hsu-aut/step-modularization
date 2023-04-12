package step;

import step.curve.Curve;

public class Circle extends Curve {

    private Axis2Placement3D position; //TODO: should be Axis2Placement that is not implemented yet
    private double radius;
    public static final String LABEL = "CIRCLE";
    public Circle(String name, String id) {
        super(name, id);
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public void setPosition(Axis2Placement3D position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
