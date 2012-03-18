package ro.koch.kolabrestapi.models;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

public class Category {
    public final String term;
    public final URI scheme;
    public final String label;

    public Category(String term, URI scheme, String label) {
        this.term = checkNotNull(term);
        this.scheme = checkNotNull(scheme);
        this.label = checkNotNull(label);
    }

    public String getTerm() { return term; }
    public URI getScheme() { return scheme; }
    public String getLabel() { return label; }
}
