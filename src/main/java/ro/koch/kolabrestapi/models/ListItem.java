package ro.koch.kolabrestapi.models;


public interface ListItem<T> {
    String getTitle();
    String getSummary();
    String getAuthor();
    String getUpdated();
    String getId(); // should be type URI
    T getPayload();
}
