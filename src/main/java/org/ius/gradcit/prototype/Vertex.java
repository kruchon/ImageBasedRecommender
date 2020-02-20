package org.ius.gradcit.prototype;

public class Vertex {

    private final String value;
    private final Type type;

    public Vertex(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        THEME,
        PHOTO,
        USER
    };

    public String toString()
    {
        return value + "-" + type;
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public boolean equals(Object o)
    {
        return (o instanceof Vertex) && (toString().equals(o.toString()));
    }

}
