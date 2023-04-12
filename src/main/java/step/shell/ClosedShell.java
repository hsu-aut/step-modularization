package step.shell;

import step.face.AdvancedFace;
import step.RepresentationItem;

import java.util.HashSet;
import java.util.Set;

public class ClosedShell extends RepresentationItem {
    public static final String LABEL = "CLOSED_SHELL";
    private Set<AdvancedFace> cfs_faces;

    public ClosedShell(String name, String id) {
        super(name, id);
        cfs_faces = new HashSet<>();
    }

    public Set<AdvancedFace> getCfs_faces() {
        return cfs_faces;
    }

    public void setCfs_faces(Set<AdvancedFace> cfs_faces) {
        this.cfs_faces = cfs_faces;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
