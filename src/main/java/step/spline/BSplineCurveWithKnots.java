package step.spline;

import java.util.List;

import step.curve.Curve;
import step.point.CartesianPoint;

public class BSplineCurveWithKnots extends Curve {

    private int degree;
    private List<CartesianPoint> control_point_list;
    private BSplineCurveForm curve_form;
    boolean closed_curve;
    boolean self_intersect;
    private List<Integer> knot_multiplicities;
    private List<Double> knots;
    private KnotType knot_spec;

    public static final String LABEL = "B_SPLINE_CURVE_WITH_KNOTS";

    public BSplineCurveWithKnots(String name, String id) {
        super(name, id);
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public List<CartesianPoint> getControl_point_list() {
        return control_point_list;
    }

    public void setControl_point_list(List<CartesianPoint> control_point_list) {
        this.control_point_list = control_point_list;
    }

    public BSplineCurveForm getCurve_form() {
        return curve_form;
    }

    public void setCurve_form(BSplineCurveForm curve_form) {
        this.curve_form = curve_form;
    }

    public boolean isClosed_curve() {
        return closed_curve;
    }

    public void setClosed_curve(boolean closed_curve) {
        this.closed_curve = closed_curve;
    }

    public boolean isSelf_intersect() {
        return self_intersect;
    }

    public void setSelf_intersect(boolean self_intersect) {
        this.self_intersect = self_intersect;
    }

    public List<Integer> getKnot_multiplicities() {
        return knot_multiplicities;
    }

    public void setKnot_multiplicities(List<Integer> knot_multiplicities) {
        this.knot_multiplicities = knot_multiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    public void setKnots(List<Double> knots) {
        this.knots = knots;
    }

    public KnotType getKnot_spec() {
        return knot_spec;
    }

    public void setKnot_spec(KnotType knot_spec) {
        this.knot_spec = knot_spec;
    }
}
