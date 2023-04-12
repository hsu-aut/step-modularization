package step.spline;

import java.util.List;

public class BSplineSurfaceWithKnots extends BSplineSurface{
    public static final String LABEL = "B_SPLINE_SURFACE_WITH_KNOTS";

    private List<Integer> u_multiplicities;
    private List<Integer> v_multiplicities;
    private List<Double> u_knots;
    private List<Double> v_knots;
    private KnotType knot_spec;

    public BSplineSurfaceWithKnots(String name, String id) {
        super(name, id);
    }

    public List<Integer> getU_multiplicities() {
        return u_multiplicities;
    }

    public void setU_multiplicities(List<Integer> u_multiplicities) {
        this.u_multiplicities = u_multiplicities;
    }

    public List<Integer> getV_multiplicities() {
        return v_multiplicities;
    }

    public void setV_multiplicities(List<Integer> v_multiplicities) {
        this.v_multiplicities = v_multiplicities;
    }

    public List<Double> getU_knots() {
        return u_knots;
    }

    public void setU_knots(List<Double> u_knots) {
        this.u_knots = u_knots;
    }

    public List<Double> getV_knots() {
        return v_knots;
    }

    public void setV_knots(List<Double> v_knots) {
        this.v_knots = v_knots;
    }

    public KnotType getKnot_spec() {
        return knot_spec;
    }

    public void setKnot_spec(KnotType knot_spec) {
        this.knot_spec = knot_spec;
    }

}
