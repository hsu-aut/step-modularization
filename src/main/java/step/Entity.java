package step;

public abstract class Entity {
    private String id;
    private final String LABEL;

    protected Entity(String id) {
        this.LABEL = "ENTITY";
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract String getEntityLabel();
}
