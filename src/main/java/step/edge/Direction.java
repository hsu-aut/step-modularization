package step.edge;

import step.RepresentationItem;

import java.util.ArrayList;
import java.util.List;

public class Direction extends RepresentationItem {
    public static final String LABEL = "DIRECTION";

    private List<Double> direction_ratios;

    public Direction(String name, String id) {
        super(name, id);
        direction_ratios = new ArrayList<>();
    }

    public List<Double> getDirection_ratios() {
        return direction_ratios;
    }

    public void setDirection_ratios(List<Double> direction_ratios) {
        this.direction_ratios = direction_ratios;
    }
}
