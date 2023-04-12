package step.curve;

import step.RepresentationItem;
import step.edge.Direction;

public class Vector extends RepresentationItem {
    public static final String LABEL = "VECTOR";
    private Direction orientation;
    private double magnitude;

    public Vector(String name, String id) {
        super(name, id);
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

}
