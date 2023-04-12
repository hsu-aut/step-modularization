package step.spline;

import step.RepresentationItem;
import step.point.Point;
import java.util.List;

public class BSplineSurface extends RepresentationItem {
    private int u_degree;
    private int v_degree;
    private List<List<Point>> control_points_list;
    private boolean u_closed;
    private boolean v_closed;
    private boolean self_intersect;
    private SurfaceForm surface_form;

    public BSplineSurface(String name, String id) {
        super(name, id);
    }

    public int getU_degree() {
        return u_degree;
    }

    public void setU_degree(int u_degree) {
        this.u_degree = u_degree;
    }

    public int getV_degree() {
        return v_degree;
    }

    public void setV_degree(int v_degree) {
        this.v_degree = v_degree;
    }

    public List<List<Point>> getControl_points_list() {
        return control_points_list;
    }

    public void setControl_points_list(List<List<Point>> control_points_list) {
        this.control_points_list = control_points_list;
    }

    public boolean isU_closed() {
        return u_closed;
    }

    public void setU_closed(boolean u_closed) {
        this.u_closed = u_closed;
    }

    public boolean isV_closed() {
        return v_closed;
    }

    public void setV_closed(boolean v_closed) {
        this.v_closed = v_closed;
    }

    public boolean isSelf_intersect() {
        return self_intersect;
    }

    public void setSelf_intersect(boolean self_intersect) {
        this.self_intersect = self_intersect;
    }

    public SurfaceForm getSurface_form() {
        return surface_form;
    }

    public void setSurface_form(SurfaceForm surface_form) {
        this.surface_form = surface_form;
    }

}
