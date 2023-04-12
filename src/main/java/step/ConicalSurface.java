package step;

public class ConicalSurface extends RepresentationItem {
    private Axis2Placement3D position;
    private double radius;
    private double semi_angle;
    public static final String LABEL = "CONICAL_SURFACE";

    public ConicalSurface(String name, String id) {
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

    public double getSemi_angle() {
        return semi_angle;
    }

    public void setSemi_angle(double semi_angle) {
        this.semi_angle = semi_angle;
    }
}
