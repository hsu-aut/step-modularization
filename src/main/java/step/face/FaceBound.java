package step.face;

import step.Loop;
import step.RepresentationItem;

public class FaceBound extends RepresentationItem {
    public static final String LABEL = "FACE_BOUND";
    private boolean orientation;
    private Loop bound;

    public FaceBound(String name, String id, boolean orientation) {
        super(name, id);
        this.orientation = orientation;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }


    public Loop getBound() {
        return bound;
    }

    public void setBound(Loop bound) {
        this.bound = bound;
    }

}
