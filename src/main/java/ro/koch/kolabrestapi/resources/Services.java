package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PATH_COLLECTIONS;

import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Service;
import org.apache.abdera.model.Workspace;

import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Inject;

public class Services {

    private final PathParams pathParams;
    private final ConnectedStorage storage;
    private final Abdera abdera;

    @Inject
    public Services(PathParams pathParams, ConnectedStorage storage, Abdera abdera) {
        this.pathParams = checkNotNull(pathParams);
        this.storage = checkNotNull(storage);
        this.abdera = checkNotNull(abdera);
    }

    @GET
    public Service get(@Context UriInfo info) {
        final Factory factory = abdera.getFactory();
        final Service service = factory.newService();
        final Workspace workspace = factory.newWorkspace();
        for(final String collection : storage.getCollections()) {
            final UriBuilder uriBuilder = info.getBaseUriBuilder();
            final org.apache.abdera.model.Collection abderaCol = factory.newCollection();
            abderaCol.setTitle(collection);
            abderaCol.setHref(uriBuilder.path(PATH_COLLECTIONS).path(collection).build().toString());
        }
        service.addWorkspace(workspace);
        return service;
    }
}
