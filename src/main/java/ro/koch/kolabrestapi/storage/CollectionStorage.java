package ro.koch.kolabrestapi.storage;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.skip;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import ro.koch.kolabrestapi.PaginationRange;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.models.Resource.Meta;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CollectionStorage {

    private final Map<String, Resource> resourcesMap = Maps.newHashMap();
    private final List<Resource> updates = Lists.newLinkedList();

    // TODO different entityTags for different PaginationRanges?
    private EntityTag entityTag = newEntityTag(null);

    public String post(Resource resource) {
        pushNew(resource);
        return resource.meta.id;
    }

    public GetResult conditionalGet(String id, Preconditions preconditions) {
        Resource resource = resourcesMap.get(id);
        if(resource==null)
            return GetResult.NOT_FOUND;
        else if(preconditions.shouldPerform(resource.meta.getETag()))
            return new GetResult(resource);
        else
            return GetResult.NOT_MODIFIED;
    }

    public boolean conditionalPut(Resource newResource, Preconditions preconditions) {
        Resource oldResource = resourcesMap.get(newResource.meta.id);
        if(!preconditions.shouldPerform(oldResource.meta.getETag())) return false;
        pushUpdate(oldResource, newResource);
        return true;
    }

    public boolean conditionalDelete(Meta meta, Preconditions preconditions) {
        Resource oldResource = resourcesMap.get(meta.id);
        if(!preconditions.shouldPerform(oldResource.meta.getETag())) return false;
        pushUpdate(oldResource, oldResource.delete(meta));
        return true;
    }

    private void removeOlderEntryFromUpdatesList(Resource resource) {
        Iterator<Resource> it = updates.iterator();
        while(it.hasNext()) {
            if(resource.equals(it.next())) {
                it.remove();
                return;
            }
        }
    }

    public ResultList listUpdates(PaginationRange range, Preconditions preconditions) {
        if(preconditions.shouldPerform(entityTag)) {
            return new ResultList(
                    limit(skip(updates, range.offset), range.limit),
                    updates.size(), entityTag
                    );
        } else {
            return ResultList.NOT_MODIFIED;
        }
    }

    private synchronized void pushUpdate(Resource oldResource, Resource newResource) {
        removeOlderEntryFromUpdatesList(oldResource);
        pushNew(newResource);
        while(purgeLatestIfTombstone()){};
    }

    private synchronized void pushNew(Resource resource) {
        updates.add(0, resource);
        resourcesMap.put(resource.meta.id, resource);
        entityTag = newEntityTag(resource.meta.updated);
    }

    private synchronized boolean purgeLatestIfTombstone() {
        int lastIndex = updates.size() - 1;
        if(lastIndex == -1) return false;
        Resource lastResource = updates.get(lastIndex);
        if(!lastResource.isDeleted()) return false;

        resourcesMap.remove(lastResource.meta.id);
        updates.remove(lastIndex);
        return true;
    }

    private EntityTag newEntityTag(DateTime updated) {
        return new EntityTag((updated == null ? "0" : updated.toString())
                + "@" + UUID.randomUUID().toString(), true);
    }

    public static class ResultList {
        public static final ResultList NOT_MODIFIED = new ResultList(Status.NOT_MODIFIED);
        public final Iterable<Resource> it;
        public final int total;
        public final Status status;
        public final EntityTag etag;

        public ResultList(Iterable<Resource> it, int total, EntityTag etag) {
            super();
            this.it = it;
            this.total = total;
            this.status = Status.OK;
            this.etag = etag;
        }

        private ResultList(Status status) {
            this.status = status;
            this.it = null;
            this.total = -1;
            this.etag = null;
        }
    }

    public static class GetResult {
        public static final GetResult NOT_FOUND = new GetResult(Status.NOT_FOUND);
        public static final GetResult NOT_MODIFIED = new GetResult(Status.NOT_MODIFIED);

        public final Status status;
        public final Resource resource;

        public GetResult(Resource resource) {
            this.status = Status.OK;
            this.resource = resource;
        }

        public GetResult(Status status) {
            this.resource = null;
            this.status = status;
        }
    }
}
