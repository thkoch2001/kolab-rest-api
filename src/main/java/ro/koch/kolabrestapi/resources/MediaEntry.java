package ro.koch.kolabrestapi.resources;

import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;
import static ro.koch.kolabrestapi.resources.EntryGetCommon.condResponse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import ro.koch.kolabrestapi.Clock;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource.Meta;
import ro.koch.kolabrestapi.models.UnparsedResource;
import ro.koch.kolabrestapi.storage.CollectionStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

public class MediaEntry {
    private final CollectionStorage storage;
    private final Clock clock;
    private final String entryId;
    private final Preconditions preconditions;

    @Inject
    public MediaEntry(PathParams pathParams, CollectionStorage storage, Clock clock, Preconditions preconditions) {
        this.entryId = pathParams.get(ENTRY);
        this.storage = storage;
        this.clock = clock;
        this.preconditions = preconditions;
    }

    @GET
    public Response get(@InjectParam EntryGetCommon getCommon) {
        return getCommon.getMediaEntryRespone();
    }

    @DELETE public Response delete() {
        return condResponse(storage.conditionalDelete(newMeta(), preconditions));
    }

    @PUT public Response put(@InjectParam ro.koch.kolabrestapi.models.Collection collection,
                             UnparsedResource unparsedResource) {
        if(!collection.isAcceptable(unparsedResource.getMediaType())) {
            // TODO list acceptable variants
            return Response.notAcceptable(null).build();
        }
        return condResponse(storage.conditionalPut(
                unparsedResource.parse(newMeta()),
                preconditions));
    }

    private Meta newMeta() {
        return new Meta(clock.get(), entryId);
    }
}
