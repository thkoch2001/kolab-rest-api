package ro.koch.kolabrestapi.storage;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.skip;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import ro.koch.kolabrestapi.PaginationRange;
import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.models.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CollectionStorage {

    private final Map<String, Resource> resourcesMap = Maps.newHashMap();
    private final List<Resource> updates = Lists.newLinkedList();

    public void post(String id, Resource resource) {
        resourcesMap.put(id, resource);
        updates.add(0, resource);
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

    public boolean conditionalPut(String id, Resource resource, DateTime timestamp, Preconditions preconditions) {
        Resource oldResource = resourcesMap.get(id);
        if(!preconditions.shouldPerform(oldResource.meta.getETag())) return false;
        removeOlderEntryFromUpdatesList(oldResource);
        Resource newResource = resource.update(resource, timestamp);
        resourcesMap.put(id, resource);
        updates.add(0, newResource);
        return true;
    }

    public boolean conditionalDelete(String id, DateTime dateTime, Preconditions preconditions) {
        Resource oldResource = resourcesMap.get(id);
        if(!preconditions.shouldPerform(oldResource.meta.getETag())) return false;
        removeOlderEntryFromUpdatesList(oldResource);
        Resource newResource = oldResource.delete(dateTime);
        updates.add(0, newResource);
        resourcesMap.put(id, newResource);
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

    public ResultList listUpdates(PaginationRange range) {
        return new ResultList(
                limit(skip(updates, range.offset), range.limit),
                updates.size()
                );
    }

    public static class ResultList {
        public final Iterable<Resource> it;
        public final int total;

        public ResultList(Iterable<Resource> it, int total) {
            super();
            this.it = it;
            this.total = total;
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
