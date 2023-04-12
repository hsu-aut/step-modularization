package step.vertex;

import step.point.Point;

public class VertexPoint extends Vertex{
    public static final String LABEL = "VERTEX_POINT";
    private Point vertex_geometry;

    public VertexPoint(String name, String id, Point vertex_geometry) {
        super(name, id);
        this.vertex_geometry = vertex_geometry;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }

    public Point getVertex_geometry() {
        return vertex_geometry;
    }

    public void setVertex_geometry(Point vertex_geometry) {
        this.vertex_geometry = vertex_geometry;
    }
}
