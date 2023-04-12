package step.shell;

import step.RepresentationItem;

import java.util.HashSet;
import java.util.Set;

public class ShellBasedSurfaceModel extends RepresentationItem {
    public static final String LABEL = "SHELL_BASED_SURFACE_MODEL";
    private Set<Shell> sbsm_boundary;

    public ShellBasedSurfaceModel(String name, String id) {
        super(name, id);
        this.sbsm_boundary = new HashSet<>();
    }


    public Set<Shell> getSbsm_boundary() {
        return sbsm_boundary;
    }

    public void setSbsm_boundary(Set<Shell> sbsm_boundary) {
        this.sbsm_boundary = sbsm_boundary;
    }
}
