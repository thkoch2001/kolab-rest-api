package ro.koch.kolabrestapi.models;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public class Collection {
    private final String title;
    private final String name;

    public Collection(String name, String title) {
        this.name = checkNotNull(name);
        this.title = checkNotNull(title);
    }

    public String getName() { return name; }
    public String getTitle() { return title; }

    @Override public String toString() {
        return toStringHelper(this)
            .add("name", name)
            .add("title", title)
            .toString();
    }

    @Override public boolean equals(Object obj) { return equal(this, obj); }

    @Override public int hashCode() {
        return Objects.hashCode(name, title);
    }
}
