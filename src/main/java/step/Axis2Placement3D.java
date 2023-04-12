package step;

import step.edge.Direction;
import step.point.CartesianPoint;

public class Axis2Placement3D extends RepresentationItem{

    private CartesianPoint location;
    private Direction axis;
    private Direction  ref_direction;

    public static final String LABEL = "AXIS2_PLACEMENT_3D";
    public Axis2Placement3D(String name, String id) {
        super(name, id);
    }

    public CartesianPoint getLocation() {
        return location;
    }

    public void setLocation(CartesianPoint location) {
        this.location = location;
    }

    public Direction getAxis() {
        return axis;
    }

    public void setAxis(Direction axis) {
        this.axis = axis;
    }

    public Direction getRef_direction() {
        return ref_direction;
    }

    public void setRef_direction(Direction ref_direction) {
        this.ref_direction = ref_direction;
    }
}
