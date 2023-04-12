package step.face;

import step.RepresentationItem;

import java.util.HashSet;

public class Face extends RepresentationItem {
    public static final String LABEL = "FACE";
    private HashSet<FaceBound> bounds;

    public Face(String name, String id) {
        super(name,id);
        this.bounds = new HashSet<FaceBound>();
    }

    public HashSet<FaceBound> getBounds() {
        return bounds;
    }

    public void setBounds(HashSet<FaceBound> bounds) {
        this.bounds = bounds;
    }

}
