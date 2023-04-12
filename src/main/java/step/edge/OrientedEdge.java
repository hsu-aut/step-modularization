package step.edge;

public class OrientedEdge extends Edge {
    public static final String LABEL = "ORIENTED_EDGE";
    private Edge edge_element;
    private boolean orientation;

    public OrientedEdge(String name, String id, boolean orientation, OrientedEdge edge_element) {
        super(name, id);
        this.orientation = orientation;
        this.edge_element = edge_element;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }

    public Edge getEdge_element() {
        return edge_element;
    }

    public void setEdge_element(Edge edge_element) {
        this.edge_element = edge_element;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }
}
