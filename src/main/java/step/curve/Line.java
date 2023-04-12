package step.curve;

import step.point.Point;

public class Line extends Curve {
    public static final String LABEL = "LINE";

    private Point pnt;
    private Vector dir;

    public Line(String name, String id) {
        super(name, id);
    }

    public Point getPnt() {
        return pnt;
    }

    public void setPnt(Point pnt) {
        this.pnt = pnt;
    }

    public Vector getDir() {
        return dir;
    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

}
