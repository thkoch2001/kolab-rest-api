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

import ro.koch.resourcefacades.FacadeFactory;
import ro.koch.resourcefacades.FacadeProvider;
import ro.koch.resourcefacades.facades.AbstractWriter.WriterFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class Resource {
    public final Meta meta;
    public final FacadeProvider facadeProvider;
    public final MediaType mediaType;

    public Resource(Meta meta, FacadeProvider facadeProvider, MediaType mediaType) {
        this.meta = checkNotNull(meta);
        this.facadeProvider = checkNotNull(facadeProvider);
        this.mediaType = checkNotNull(mediaType);
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

    public void asMediaType(final MediaType mediaType, final OutputStream out) throws IOException {
        ro.koch.resourcefacades.Writer writer = facadeProvider.getFacade(ro.koch.resourcefacades.Writer.class, new Predicate<FacadeFactory<?>>() {
         @Override public boolean apply(FacadeFactory<?> facadeFactory) {
        return ((WriterFactory)facadeFactory).isWriteable(mediaType);
         }
      });
        if(null == writer) throw new RuntimeException("no writer for mediatype: "+mediaType);
        writer.writeTo(out);
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

    public <T> T getFacade(Class<T> clazz) {
        return facadeProvider.getFacade(clazz);
    }


}
