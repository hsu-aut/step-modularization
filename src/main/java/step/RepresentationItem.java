package step;

public abstract class RepresentationItem extends Entity {
    public static final String LABEL = "REPRESENTATION_ITEM";
    private String name;

    public RepresentationItem(String name, String id) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEntityLabel() {
        return LABEL;
    }
}
