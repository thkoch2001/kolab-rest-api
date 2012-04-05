package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Functions.toStringFunction;
import static com.google.common.collect.Iterables.transform;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_ATOMSERVICE_XML;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.factory.Factory;
import org.apache.abdera2.model.Categories;
import org.apache.abdera2.model.Service;
import org.apache.abdera2.model.Workspace;

import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.models.Category;
import ro.koch.kolabrestapi.models.Collection;
import ro.koch.kolabrestapi.storage.ConnectedStorage;

import com.google.inject.Injector;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

public class Services {

    @GET @Produces({APPLICATION_ATOMSERVICE_XML,APPLICATION_XML})
    public Service get(@InjectParam LinkBuilder linkBuilder,
                       @InjectParam ConnectedStorage storage,
                       @InjectParam Abdera abdera) {
        Factory factory = abdera.getFactory();
        Service service = factory.newService();
        Workspace workspace = factory.newWorkspace();

        for(Collection collection : storage.getCollections()) {
            org.apache.abdera2.model.Collection abderaCol = factory.newCollection();
            abderaCol.setTitle(collection.getTitle());
            abderaCol.setHref(linkBuilder.collectionUri(collection.getName()).toString());
            abderaCol.setAccept(transform(collection.getAccepts(), toStringFunction()));
            abderaCol.addCategories(buildCategories(abdera, collection));
            workspace.addCollection(abderaCol);
        }

        service.addWorkspace(workspace);
        return service;
    }

    @GET @Produces("text/html,application/xhtml+xml")
    public Viewable get(@InjectParam Injector inj) {
        return new Viewable("index", getServiceDoc(inj), Service.class);
    }

    private Service getServiceDoc(Injector inj) {
        return get(inj.getInstance(LinkBuilder.class),
                   inj.getInstance(ConnectedStorage.class),
                   inj.getInstance(Abdera.class));
    }

    private Categories buildCategories(Abdera abdera, Collection collection) {
        Categories categories = abdera.getFactory().newCategories();
        for(Category category : collection.getCategories()) {
            categories.addCategory(category.getScheme().toString(), category.getTerm(), category.getLabel());
        }
        return categories;
    }
}
