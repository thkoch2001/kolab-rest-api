package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.GET;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;

public class Collection {
    private final String authority;
    private final String collection;

    public Collection(String authority, String collection) {
        this.authority = checkNotNull(authority);
        this.collection = checkNotNull(collection);
    }

    @GET
    public Feed get() {
        final Feed feed = Abdera.getInstance().newFeed();
        feed.setTitle(authority + " sometitle "+collection);
        return feed;
    }
}
