package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.model.Service;
import org.apache.abdera2.model.Workspace;

import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.models.Collection;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Inject;
import com.sun.jersey.api.core.InjectParam;

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

    @GET @Produces({APPLICATION_ATOM_XML,APPLICATION_XML})
    public Service get(@InjectParam LinkBuilder linkBuilder) {
        final Factory factory = abdera.getFactory();
        final Service service = factory.newService();
        final Workspace workspace = factory.newWorkspace();
        for(final Collection collection : storage.getCollections()) {
            final org.apache.abdera2.model.Collection abderaCol = factory.newCollection();
            abderaCol.setTitle(collection.getTitle());
            abderaCol.setHref(linkBuilder.collectionUri(collection.getName()).toString());
            workspace.addCollection(abderaCol);
        }
        service.addWorkspace(workspace);
        return service;
    }
}
