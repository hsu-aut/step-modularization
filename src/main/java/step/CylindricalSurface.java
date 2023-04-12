package step;

public class CylindricalSurface extends RepresentationItem{
    public static final String LABEL = "CYLINDRICAL_SURFACE";
    private Axis2Placement3D position;
    private double radius;
    private double[] origin = {0.0, 0.0, 0.0};
    private double[] direction = {0.0, 0.0, 0.0};
    
    public CylindricalSurface(String name, String id) {
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
    
    public double[] getOrigin() {
      return origin;
    }
    
    public void setOrigin(double[] origin) {
      this.origin = origin;
    }
    
    public double[] getDirection() {
      return direction;
    }
    
    public void setDirection(double[] direction) {
      this.direction = direction;
    }

}
