package ro.koch.kolabrestapi.resources;

import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.GetResult;

import com.sun.jersey.api.core.InjectParam;

public class MediaEntry {
    @GET
    public Response get(@InjectParam CollectionStorage storage,
                        @InjectParam PathParams pathParams,
                        @InjectParam Preconditions preconditions
            ) {
        GetResult getResult = storage.conditionalGet(pathParams.get(ENTRY), preconditions);
        ResponseBuilder rb = Response.status(getResult.status);

        if(getResult.status == Status.OK) {
            rb.entity(getResult.resource)
              .tag(getResult.resource.meta.getETag())
              .type(getResult.resource.mediaType);
        }
        return rb.build();
    }
}
