package step.face;

import step.Axis2Placement3D;
import step.RepresentationItem;

public class SphericalSurface extends RepresentationItem {
    public static final String LABEL = "SPHERICAL_SURFACE";
    private double radius;
    private Axis2Placement3D position;
    
    public SphericalSurface(String name, String id) {
        super(name, id);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public void setPosition(Axis2Placement3D position) {
        this.position = position;
    }

}
