package ro.koch.kolabrestapi.resources;

import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.sun.jersey.api.core.InjectParam;

public class MediaEntry {
    @GET
    public Response get(@InjectParam ConnectedStorage connectedStorage,
                          @InjectParam PathParams pathParams
            ) {
        final CollectionStorage collectionStorage = connectedStorage.getConnectionStorage(pathParams.get(COLLECTION));
        final Resource res = collectionStorage.conditionalGet(pathParams.get(MEMBER), null);
        return Response.ok(res, res.mediaType).build();
    }
}
