package step;

import step.RepresentationItem;
import step.spline.GeometricSetSelect;

import java.util.Set;

public class GeometricCurveSet extends RepresentationItem {
    public static final String LABEL = "GEOMETRIC_CURVE_SET";
    private Set<GeometricSetSelect> elements;

    public GeometricCurveSet(String name, String id) {
        super(name, id);
    }

    public Set<GeometricSetSelect> getElements() {
        return elements;
    }

    public void setElements(Set<GeometricSetSelect> elements) {
        this.elements = elements;
    }

}
