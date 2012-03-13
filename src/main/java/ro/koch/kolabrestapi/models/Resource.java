package ro.koch.kolabrestapi.models;

public interface Resource {
    public Meta meta();

    public interface Meta {
        long updated();
    }
}
