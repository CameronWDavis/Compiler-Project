public class Symbol {
    private final String name;
    private final String type;
    private final String value;

    public Symbol(String name, String type) {
        this(name, type, null);
    }

    public Symbol(String name, String type, String value) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "name " + name + " type " + type + (value != null ? " value " + value : "");
    }
}
