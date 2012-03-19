package ro.koch.kolabrestapi.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Variant;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.OutputSupplier;

public class Resource {
    public final Meta meta;
    public final byte[] body;
    public final MediaType mediaType;

    public Resource(Meta meta, byte[] body, MediaType mediaType) {
        this.meta = meta;
        this.body = body;
        this.mediaType = mediaType;
    }

    public Resource delete(Meta meta) {
        return new Resource(meta, null, null);
    }

    public boolean isDeleted() {
        return mediaType == null;
    }

    public Variant selectVariant(Request request) {
        return request.selectVariant(availableVariants());
    }

    public void asMediaType(MediaType mediaType, final OutputStream out) throws IOException {
        ByteStreams.write(body, new OutputSupplier<OutputStream>(){
            @Override public OutputStream getOutput() throws IOException {return out;}
          }
        );
    }

    public List<Variant> availableVariants() {
        return Lists.newArrayList(new Variant(mediaType, null, null),
                                  new Variant(new MediaType("text","obscure"),null,null));
    }

    public static class Meta {
        public final DateTime updated;
        public final String id;

        public Meta(DateTime dateTime, String id) {
            this.updated = checkNotNull(dateTime);
            this.id = checkNotNull(emptyToNull(id));
        }

        public EntityTag getETag() {
            // TODO for production it is not save enough to just use the timestamp
            return new EntityTag(updated.toString(),true);
        }
    }


}
