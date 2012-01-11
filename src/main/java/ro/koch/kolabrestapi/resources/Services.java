package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Service;

@Produces(APPLICATION_ATOM_XML)
public class Services {

    private final String authority;

    public Services(String authority) {
        this.authority = checkNotNull(authority);
    }

    @GET
    public Service get() {
        final Service service = Abdera.getInstance().newService();
        service.addWorkspace("testworkspace for "+authority);
        return service;
    }
}
