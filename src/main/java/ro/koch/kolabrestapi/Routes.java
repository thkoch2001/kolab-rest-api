package ro.koch.kolabrestapi;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ro.koch.kolabrestapi.resources.Collection;
import ro.koch.kolabrestapi.resources.Services;

@Path("")
public class Routes {

    @Path("{authority}/service")
    public Services service(@PathParam("authority") String authority) {
        return new Services(authority);
    }

    @Path("{authority}/collections/{collection}")
    public Collection collection(@PathParam("authority") String authority,
            @PathParam("collection") String collection ) {
        return new Collection(authority, collection);
    }

}
