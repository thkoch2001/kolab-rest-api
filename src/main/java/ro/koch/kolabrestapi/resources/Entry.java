package ro.koch.kolabrestapi.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_ATOMDELETED_XML;
import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class Entry {
    private final CollectionStorage storage;
    private final Clock clock;
    private final String entryId;
    private final Preconditions preconditions;

    @Inject
    public Entry(PathParams pathParams, CollectionStorage storage, Clock clock, Preconditions preconditions) {
        this.entryId = pathParams.get(ENTRY);
        this.storage = storage;
        this.clock = clock;
        this.preconditions = preconditions;
    }

    @DELETE public Response delete() {
        return condResponse(storage.conditionalDelete(entryId, clock.get(), preconditions));
    }

    @PUT public Response put(Resource resource) {
        return condResponse(storage.conditionalPut(resource, clock.get(), preconditions));
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML,APPLICATION_ATOMDELETED_XML})
    public Response get(@InjectParam ResourceAbderaAdapter abderaAdapter,
                        @InjectParam EntryGetCommon getCommon) {
        return getCommon.getEntryResponse(abderaAdapter);
    }

    private static Response condResponse(boolean conditionOk) {
        return Response.status(conditionOk ? NO_CONTENT : PRECONDITION_FAILED).build();
    }
}
