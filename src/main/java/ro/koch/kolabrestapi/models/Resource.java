package ro.koch.kolabrestapi.models;

import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

public class Resource {
    public final Meta meta;
    public final byte[] body;
    public final MediaType mediaType;

    public Resource(Meta meta, byte[] body, MediaType mediaType) {
        this.meta = meta;
        this.body = body;
        this.mediaType = mediaType;
    }

    public Resource delete(DateTime timestamp) {
        return new Resource(new Meta(timestamp, meta.id), null, null);
    }

    public Resource update(Resource resource, DateTime timestamp) {
        return new Resource(new Meta(timestamp, meta.id), body, mediaType);
    }

    public boolean isDeleted() {
        return mediaType == null;
    }

    public static class Meta {
        public final DateTime updated;
        public final String id;

        public Meta(DateTime dateTime, String id) {
            this.updated = dateTime;
            this.id = id;
        }
    }


}
