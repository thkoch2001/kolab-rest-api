package ro.koch.kolabrestapi;

import static com.google.common.base.Preconditions.checkNotNull;
import static ro.koch.kolabrestapi.Routes.PathTemplate.COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.T_COLLECTION;
import static ro.koch.kolabrestapi.Routes.PathTemplate.T_ENTRY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.T_MEDIAENTRY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.T_SERVICE;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import ro.koch.kolabrestapi.resources.Collection;
import ro.koch.kolabrestapi.resources.Entry;
import ro.koch.kolabrestapi.resources.MediaEntry;
import ro.koch.kolabrestapi.resources.Services;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Path("") @Singleton
public class Routes {

    private final Injector injector;

    @Inject public Routes(Injector injector) {
        this.injector = checkNotNull(injector);
    }

    @Path(T_SERVICE)
    public Services service() { return serve(Services.class); }

    @Path(T_COLLECTION)
    public Collection collection() { return serve(Collection.class); }

    @Path(T_ENTRY)
    public Entry entry() { return serve(Entry.class); }

    @Path(T_MEDIAENTRY)
    public MediaEntry mediaEntry() { return serve(MediaEntry.class); }


    @SuppressWarnings("unchecked")
    private <K> K serve(Class<?> clazz) {
        return (K)injector.getInstance(clazz);
    }

    public static class PathParams {
        private final ImmutableMap<String, ? extends Object> map;

        @Inject
        public PathParams(UriInfo uriInfo) {
            ImmutableMap.Builder<String, String> pathParams = ImmutableMap.builder();
            for(Map.Entry<String, List<String>> entry : uriInfo.getPathParameters().entrySet()) {
                pathParams.put(entry.getKey(), entry.getValue().get(0));
            }
            map = pathParams.build();
        }

        public String get(String name) { return map.get(name).toString(); }

        public Map<String, Object> merge(Map<String, ? extends Object> other) {
            Map<String, Object> merged = Maps.newHashMap(map);
            merged.putAll(other);
            return ImmutableMap.copyOf(merged);
        }
    }

    public static class PathTemplate {
        public static final String AUTHORITY = "AUTHORITY";
        public static final String COLLECTION = "COLLECTION";
        public static final String ENTRY = "ENTRY";
        public static final String SEG_SERVICE = "service";
        public static final String SEG_COLLECTIONS = "collections";
        public static final String SEG_MEDIA = "media";

        private static final String AUTHBASE = "/{"+AUTHORITY+"}/";
        public static final String T_SERVICE = AUTHBASE+SEG_SERVICE;
        public static final String T_COLLECTION = AUTHBASE+SEG_COLLECTIONS+"/{"+COLLECTION+"}";
        public static final String T_ENTRY = T_COLLECTION+"/{"+ENTRY+"}";
        public static final String T_MEDIAENTRY = AUTHBASE+SEG_MEDIA+"/{"+COLLECTION+"}/{"+ENTRY+"}";
    }


    public static class LinkBuilder {
        private final UriInfo uriInfo;
        private final PathParams pathParams;

        @Inject
        public LinkBuilder(UriInfo uriInfo, PathParams pathParams) {
            this.uriInfo = uriInfo;
            this.pathParams = pathParams;
        }

        public String getParam(String name) {
            return pathParams.get(name);
        }

        public URI collectionUri(String collection) {
            return build(T_COLLECTION, ImmutableMap.of(COLLECTION, collection));
        }

        public URI entryUri(String id) {
            return build(T_ENTRY, ImmutableMap.of(ENTRY, id));
        }

        public URI mediaEntryUri(String id) {
            return build(T_MEDIAENTRY, ImmutableMap.of(ENTRY, id));
        }

        public URI next(PaginationRange range) {
            return uriInfo.getRequestUriBuilder()
                    .replaceQueryParam("offset", range.nextOffset())
                    .build();
        }

        public URI build(String template, ImmutableMap<String, String> parameters) {
            return uriInfo.getBaseUriBuilder()
                    .path(template)
                    .buildFromMap(pathParams.merge(parameters));
        }
    }
}
