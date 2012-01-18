package ro.koch.kolabrestapi.models;

public interface ConvertibleContact <T> {
    ConvertibleContact from(Object in);
}
