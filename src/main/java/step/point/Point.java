package step.point;

import step.RepresentationItem;

public class Point extends RepresentationItem {
    private static final String LABEL = "POINT";

    public Point(String name, String id) {
        super(name, id);
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
