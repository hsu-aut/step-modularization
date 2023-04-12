package step;

import step.point.CartesianPoint;

import java.util.ArrayList;
import java.util.List;

public class Polyline extends RepresentationItem{

    private List<CartesianPoint> points;
    public static final String LABEL = "POLYLINE";
    public Polyline(String name, String id) {
        super(name, id);
        points = new ArrayList<>();
    }

    public List<CartesianPoint> getPoints() {
        return points;
    }

    public void setPoints(List<CartesianPoint> points) {
        this.points = points;
    }

}
