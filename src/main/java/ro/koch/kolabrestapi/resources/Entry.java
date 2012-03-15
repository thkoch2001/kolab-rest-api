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
        return Response.noContent().build();
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
        return Response.noContent().build();
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam Abdera abdera,
                        @InjectParam LinkBuilder linkBuilder,
                        @InjectParam ConnectedStorage connectedStorage,
                        @InjectParam PathParams pathParams) {
        String collection = pathParams.get(COLLECTION);
        CollectionStorage collectionStorage = connectedStorage.getConnectionStorage(collection);
        Resource resource = collectionStorage.conditionalGet(pathParams.get(MEMBER), null);
        ResourceAbderaAdapter adapter = new ResourceAbderaAdapter(abdera, linkBuilder, collection);
        return Response.ok(adapter.buildFeedElement(resource)).build();
    }
}
