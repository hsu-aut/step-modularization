package step.edge;

import step.Loop;

import java.util.ArrayList;
import java.util.List;

public class EdgeLoop extends Loop {
    public static final String LABEL = "EDGE_LOOP";
    private List<OrientedEdge> edge_list;

    public EdgeLoop(String name, String id) {
        super(name, id);
        this.edge_list = new ArrayList<>();
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
