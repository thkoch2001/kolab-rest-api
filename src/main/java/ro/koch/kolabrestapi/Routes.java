package ro.koch.kolabrestapi;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ro.koch.kolabrestapi.resources.Collection;
import ro.koch.kolabrestapi.resources.Member;
import ro.koch.kolabrestapi.resources.Services;
import ro.koch.kolabrestapi.storage.ConnectedStorage;
import ro.koch.kolabrestapi.storage.Storages;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;

@Path("")
public class Routes {
    public static final String PATH_COLLECTIONS = "/collections";
    public static final String PATH_SERVICE = "/service";

    private final Injector injector;
    private final ImmutableMap.Builder<String, String> pathParams = ImmutableMap.builder();

    @Inject
    public Routes(Injector injector) {
        this.injector = checkNotNull(injector);
    }

    @Path("/{"+AUTHORITY+"}/")
    public Authority authority(@PathParam(AUTHORITY) String authority) {
        pathParams.put(AUTHORITY, authority);
        return new Authority();
    }

    public class Authority {
        @Path(PATH_SERVICE)
        public Services service() {
            return serve(Services.class);
        }

        @Path(PATH_COLLECTIONS+"/{"+COLLECTION+"}")
        public Collection collection(@PathParam(COLLECTION) String collection) {
            pathParams.put(COLLECTION, collection);
            return serve(Collection.class);
        }

        @Path(PATH_COLLECTIONS+"/{"+COLLECTION+"}/{"+MEMBER+"}/")
        public Member collection(@PathParam(COLLECTION) String collection, @PathParam(MEMBER) String member) {
            pathParams.put(COLLECTION, collection);
            pathParams.put(MEMBER, member);
            return serve(Member.class);
        }
    }

    @SuppressWarnings("unchecked")
    private <K> K serve(Class<?> clazz) {
        return (K)injector.createChildInjector(
        new AbstractModule(){
            @Override protected void configure() {
                bind(PathParams.class).toInstance(new PathParams(pathParams.build()));
            }

            @SuppressWarnings("unused")
            @Provides public ConnectedStorage connectedStorage(Storages storages, PathParams pathParams) {
                return storages.getForAuthority(pathParams.get(AUTHORITY));
            }
        }
        ).getInstance(clazz);
    }

    public class PathParams {
        public static final String AUTHORITY = "authority";
        public static final String COLLECTION = "collection";
        public static final String MEMBER = "member";

        private final ImmutableMap<String, String> map;

        public PathParams(ImmutableMap<String, String> pathParams) {
            this.map = checkNotNull(pathParams);
        }

        public String get(String name) {
            return map.get(name);
        }
    }
}
