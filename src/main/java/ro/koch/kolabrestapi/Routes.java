package ro.koch.kolabrestapi;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathParams.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathParams.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathParams.MEMBER;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import ro.koch.kolabrestapi.resources.Collection;
import ro.koch.kolabrestapi.resources.Entry;
import ro.koch.kolabrestapi.resources.MediaEntry;
import ro.koch.kolabrestapi.resources.Services;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Path("") @Singleton
public class Routes {
    public static final String PATH_COLLECTIONS = "/collections";
    public static final String PATH_SERVICE = "/service";
    public static final String PATH_MEDIA = "/media";

    private final Injector injector;

    @Inject
    public Routes(Injector injector) {
        this.injector = checkNotNull(injector);
    }

    @Path("/{"+AUTHORITY+"}")
    public Authority authority() {
        return new Authority();
    }

    public class Authority {
        @Path(PATH_SERVICE)
        public Services service() {
            return serve(Services.class);
        }

        @Path(PATH_COLLECTIONS+"/{"+COLLECTION+"}")
        public Collection collection() {
            return serve(Collection.class);
        }

        @Path(PATH_COLLECTIONS+"/{"+COLLECTION+"}/{"+MEMBER+"}")
        public Entry entry() {
            return serve(Entry.class);
        }

        @Path(PATH_MEDIA+"/{"+COLLECTION+"}/{"+MEMBER+"}")
        public MediaEntry mediaEntry() {
            return serve(MediaEntry.class);
        }
    }

    @SuppressWarnings("unchecked")
    private <K> K serve(Class<?> clazz) {
        return (K)injector.getInstance(clazz);
    }

    public static class PathParams {
        public static final String AUTHORITY = "authority";
        public static final String COLLECTION = "collection";
        public static final String MEMBER = "member";

        private final ImmutableMap<String, String> map;

        @Inject
        public PathParams(UriInfo uriInfo) {
            final ImmutableMap.Builder<String, String> pathParams = ImmutableMap.builder();
            for(Map.Entry<String, List<String>> entry : uriInfo.getPathParameters().entrySet()) {
                pathParams.put(entry.getKey(), entry.getValue().get(0));
            }
            this.map = pathParams.build();
        }

        public String get(String name) {
            return map.get(name);
        }
    }

    public static class LinkBuilder {
        private final UriInfo uriInfo;
        private final PathParams pathParams;

        @Inject
        public LinkBuilder(UriInfo uriInfo, PathParams pathParams) {
            this.uriInfo = uriInfo;
            this.pathParams = pathParams;
        }

        public URI collectionUri(String collection) {
            final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
            return uriBuilder
                    .path(pathParams.get(AUTHORITY))
                    .path(PATH_COLLECTIONS)
                    .path(collection)
                    .build();
        }

        public URI entryUri(String collection, String id) {
            final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
            return uriBuilder
                    .path(pathParams.get(AUTHORITY))
                    .path(PATH_COLLECTIONS)
                    .path(collection)
                    .path(id)
                    .build();
        }

        public URI mediaEntryUri(String collection, String id) {
            final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
            return uriBuilder
                    .path(pathParams.get(AUTHORITY))
                    .path(PATH_MEDIA)
                    .path(collection)
                    .path(id)
                    .build();
        }
    }
}
