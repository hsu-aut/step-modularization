package step.topology;

public class AssemblyUsage {
    String parent;
    String child;

    public AssemblyUsage(String user, String used) {
        this.parent = user;
        this.child = used;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

}
