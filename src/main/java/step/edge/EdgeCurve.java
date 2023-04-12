package step.edge;

import step.curve.Curve;

/**
 * #1591=EDGE_CURVE('',#1405,#1406,#970,.T.);
 */
public class EdgeCurve extends Edge{
    public static final String LABEL = "EDGE_CURVE";
    private boolean same_sense;
    private Curve edge_geometry;

    public EdgeCurve(String name, String id, boolean same_sense) {
        super(name, id);
        this.same_sense = same_sense;
    }

    public boolean isSame_sense() {
        return same_sense;
    }

    public void setSame_sense(boolean same_sense) {
        this.same_sense = same_sense;
    }

    public Curve getEdge_geometry() {
        return edge_geometry;
    }

    public void setEdge_geometry(Curve edge_geometry) {
        this.edge_geometry = edge_geometry;
    }

}
