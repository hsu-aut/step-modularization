package step.shell;

import step.RepresentationItem;
import step.face.Face;

import java.util.Set;

public class OpenShell extends RepresentationItem {

    private Set<Face> cfs_faces;
    public static final String LABEL = "OPEN_SHELL";
    public OpenShell(String name, String id) {
        super(name, id);
    }


    public Set<Face> getCfs_faces() {
        return cfs_faces;
    }

    public void setCfs_faces(Set<Face> cfs_faces) {
        this.cfs_faces = cfs_faces;
    }
}
