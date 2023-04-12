package step.face;

import step.Axis2Placement3D;
import step.RepresentationItem;

public class ToroidalSurface extends RepresentationItem {
    public static final String LABEL = "TOROIDAL_SURFACE";
    private double major_radius;
    private double minor_radius;
    private Axis2Placement3D position;
    public ToroidalSurface(String name, String id) {
        super(name, id);
    }

    public double getMajor_radius() {
        return major_radius;
    }

    public void setMajor_radius(double major_radius) {
        this.major_radius = major_radius;
    }

    public double getMinor_radius() {
        return minor_radius;
    }

    public void setMinor_radius(double minor_radius) {
        this.minor_radius = minor_radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public void setPosition(Axis2Placement3D position) {
        this.position = position;
    }

}
