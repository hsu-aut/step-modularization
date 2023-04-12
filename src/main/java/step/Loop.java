package step;

public abstract class Loop extends RepresentationItem {
    private final String LABEL = "LOOP";
    public Loop(String name, String id) {
        super(name, id);
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
