package ro.koch.kolabrestapi.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.iri.IRI;
import org.joda.time.DateTime;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.sun.jersey.api.core.InjectParam;

public class Entry {
    @DELETE
    public Response get(@InjectParam ConnectedStorage connectedStorage,
                        @InjectParam PathParams pathParams,
                        @InjectParam Clock clock
            ) {
        final CollectionStorage collectionStorage = connectedStorage.getConnectionStorage(pathParams.get(COLLECTION));
        collectionStorage.conditionalDelete(pathParams.get(MEMBER), clock.get(), null);
        return Response.ok().build();
    }

    @PUT
    public Response put(@InjectParam ConnectedStorage connectedStorage,
                        @InjectParam PathParams pathParams,
                        @InjectParam Clock clock,
                        Resource resource
            ) {
        String collection = pathParams.get(COLLECTION);
        CollectionStorage collectionStorage = connectedStorage.getConnectionStorage(collection);
        collectionStorage.conditionalPut(pathParams.get(MEMBER), resource, clock.get(), null);
        return Response.ok().build();
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam Abdera abdera,
                        @InjectParam LinkBuilder linkBuilder,
                        @InjectParam ConnectedStorage connectedStorage,
                        @InjectParam PathParams pathParams) {
        String collection = pathParams.get(COLLECTION);
        CollectionStorage collectionStorage = connectedStorage.getConnectionStorage(collection);
        Resource resource = collectionStorage.conditionalGet(pathParams.get(MEMBER), null);
        org.apache.abdera2.model.Entry entry = buildEntry(abdera, linkBuilder, collection, resource);
        return Response.ok(entry).build();
    }

    public static org.apache.abdera2.model.Entry buildEntry(Abdera abdera, LinkBuilder linkBuilder, final String collection, final Resource resource) {
        final org.apache.abdera2.model.Entry entry = abdera.newEntry();
        //entry.addAuthor(item.getAuthor());
        //entry.setTitle(item.getTitle());
        entry.setUpdated(new DateTime(resource.meta.updated));
        entry.setId(resource.meta.id);
        entry.setEdited(new DateTime(resource.meta.updated));
        entry.addLink(new IRI(linkBuilder.mediaEntryUri(collection, resource.meta.id)),
                      "edit-media",
                      resource.mediaType.toString(),
                      null, null, -1l);
        entry.addLink(new IRI(linkBuilder.entryUri(collection, resource.meta.id)), "edit");
        //entry.setSummary(item.getSummary());
        return entry;
    }
}
