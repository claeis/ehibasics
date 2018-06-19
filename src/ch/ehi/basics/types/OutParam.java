package ch.ehi.basics.types;

public final class OutParam<T> implements java.io.Serializable {
    private static final long serialVersionUID = 7314882565770822973L;
    public T value;

    public OutParam() {
    }

    public OutParam(T value) {
        this.value = value;
    }
}