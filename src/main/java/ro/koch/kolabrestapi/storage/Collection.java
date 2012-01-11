package ro.koch.kolabrestapi.storage;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;

public class Collection {
    private final String workspace;
    private final String name;
    private final String title;
    private final ImmutableSet<String> accepts;

    public Collection(String workspace, String name, String title, ImmutableSet<String> accepts) {
        super();
        this.workspace = checkNotNull(workspace);
        this.name = checkNotNull(name);
        this.title = checkNotNull(title);
        this.accepts = checkNotNull(accepts);
    }

    public String getWorkspace() { return workspace; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public ImmutableSet<String> getAccepts() { return accepts; }

    @Override public String toString() {
        return toStringHelper(this)
            .add("workspace", workspace)
            .add("name", name)
            .add("title", title)
            .add("accepts", accepts)
            .toString();
    }

    @Override public boolean equals(Object obj) { return equal(this, obj); }

    @Override public int hashCode() {
        return Objects.hashCode(workspace, name, title, accepts);
    }

}