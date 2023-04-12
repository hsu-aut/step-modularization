package step;

import step.shell.ClosedShell;

public class ManifoldSolidBrep extends RepresentationItem{
    public static final String LABEL = "MANIFOLD_SOLID_BREP";
    private ClosedShell outer;

    public ManifoldSolidBrep(String name, ClosedShell outer, String id) {
        super(name, id);
        this.outer = outer;
    }

    public ClosedShell getOuter() {
        return outer;
    }

    public void setOuter(ClosedShell outer) {
        this.outer = outer;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
