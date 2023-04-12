package step.shell;

import step.shell.ClosedShell;

public class OrientedClosedShell extends ClosedShell {
    private final String LABEL = "ORIENTED_CLOSED_SHELL";
    private boolean orientation;
    private ClosedShell closed_shell_element;

    public OrientedClosedShell(String name, String id, ClosedShell closed_shell_element, boolean orientation) {
        super(name,id);
        this.closed_shell_element = closed_shell_element;
        this.orientation = orientation;
    }


    public boolean isOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }


    public ClosedShell getClosed_shell_element() {
        return closed_shell_element;
    }
}
