package step.edge;

import step.RepresentationItem;
import step.vertex.Vertex;

public class Edge extends RepresentationItem {
    private static final String LABEL = "EDGE";
    private Vertex edge_start;
    private Vertex edge_end;

    public Edge(String name, String id) {
        super(name, id);
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }

    public Vertex getEdge_start() {
        return edge_start;
    }

    public void setEdge_start(Vertex edge_start) {
        this.edge_start = edge_start;
    }

    public Vertex getEdge_end() {
        return edge_end;
    }

    public void setEdge_end(Vertex edge_end) {
        this.edge_end = edge_end;
    }
}
