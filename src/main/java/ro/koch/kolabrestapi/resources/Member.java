package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import javax.ws.rs.GET;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.model.Entry;

import ro.koch.kolabrestapi.Routes.PathParams;

import com.google.inject.Inject;

public class Member {
    private final Abdera abdera;
    private final PathParams pathParams;

    @Inject
    public Member(Abdera abdera, PathParams pathParams) {
        this.pathParams = checkNotNull(pathParams);
        this.abdera = checkNotNull(abdera);
    }

    @GET
    public Entry get() {
        final Entry entry = abdera.newEntry();
        entry.addAuthor(pathParams.get(AUTHORITY));
        entry.setContent(pathParams.get(MEMBER));
        return entry;
    }
}
