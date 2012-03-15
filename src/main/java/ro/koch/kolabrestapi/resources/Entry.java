package ro.koch.kolabrestapi.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.abdera2.Abdera;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.GetResult;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class Entry {
    private final ConnectedStorage connectedStorage;
    private final Clock clock;
    private final String collection;
    private final String member;
    private final Preconditions preconditions;

    @Inject
    public Entry(PathParams pathParams, ConnectedStorage connectedStorage, Clock clock, Preconditions preconditions) {
        this.collection = pathParams.get(COLLECTION);
        this.member = pathParams.get(MEMBER);
        this.connectedStorage = connectedStorage;
        this.clock = clock;
        this.preconditions = preconditions;
    }

    @DELETE public Response delete() {
        return condResponse(storage().conditionalDelete(member, clock.get(), preconditions));
    }

    @PUT public Response put(Resource resource) {
        return condResponse(storage().conditionalPut(member, resource, clock.get(), preconditions));
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Response get(@InjectParam Abdera abdera,
                        @InjectParam LinkBuilder linkBuilder
                        ) {
        GetResult getResult = storage().conditionalGet(member, preconditions);
        ResponseBuilder rb = Response.status(getResult.status);

        if(getResult.status == Status.OK) {
            rb
             .tag(getResult.resource.meta.getETag())
             .entity((new ResourceAbderaAdapter(abdera, linkBuilder, collection))
                      .buildFeedElement(getResult.resource));
        }
        return rb.build();
    }

    private CollectionStorage storage() {
        return connectedStorage.getConnectionStorage(collection);
    }

    private static Response condResponse(boolean conditionOk) {
        return Response.status(conditionOk ? NO_CONTENT : PRECONDITION_FAILED).build();
    }
}
