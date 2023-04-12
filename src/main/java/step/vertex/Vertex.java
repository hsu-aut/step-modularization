package step.vertex;

import step.RepresentationItem;

public abstract class Vertex extends RepresentationItem {
    private static final String LABEL = "VERTEX";

    public Vertex(String name, String id) {
        super(name, id);
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
