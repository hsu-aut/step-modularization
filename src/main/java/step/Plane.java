package step;

public class Plane extends RepresentationItem{
    private Axis2Placement3D position;
    public static final String LABEL = "PLANE";
    public Plane(String name, String id) {
        super(name, id);
    }
    public Axis2Placement3D getPosition() {
        return position;
    }

    public void setPosition(Axis2Placement3D position) {
        this.position = position;
    }

}
