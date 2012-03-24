package ro.koch.kolabrestapi.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_ATOMDELETED_XML;
import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;
import static ro.koch.kolabrestapi.resources.EntryGetCommon.condResponse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource.Meta;
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
        return condResponse(storage.conditionalDelete(newMeta(), preconditions));
    }

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML,APPLICATION_ATOMDELETED_XML})
    public Response get(@InjectParam ResourceAbderaAdapter abderaAdapter,
                        @InjectParam EntryGetCommon getCommon) {
        return getCommon.getEntryResponse(abderaAdapter);
    }

    private Meta newMeta() {
        return new Meta(clock.get(), entryId);
    }
}
