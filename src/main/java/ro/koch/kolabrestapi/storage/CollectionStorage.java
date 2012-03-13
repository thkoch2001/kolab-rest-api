package ro.koch.kolabrestapi.storage;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.skip;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ro.koch.kolabrestapi.models.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CollectionStorage {

    private final Map<String, Resource> resourcesMap = Maps.newHashMap();
    private final List<StoredResource> updates = Lists.newLinkedList();

    public void create(String id, Resource resource) {
        resourcesMap.put(id, resource);
        updates.add(0, new StoredResource(resource.meta().updated(), id, resource));
    }

    public Resource conditionalGet(String id, String etag) {
        return resourcesMap.get(id);
    }

    public void conditionalPut(String id, Resource resource, String etag) {
        resourcesMap.put(id, resource);
        removeOlderEntryFromUpdatesList(resource);
        updates.add(0, new StoredResource(resource.meta().updated(), id, resource));
    }

    public void conditionalDelete(String id, long updated, String etag) {
        Resource resource = resourcesMap.remove(id);
        removeOlderEntryFromUpdatesList(resource);
        updates.add(0, new StoredResource(updated, id, null));
    }

    private void removeOlderEntryFromUpdatesList(Resource resource) {
        Iterator<StoredResource> it = updates.iterator();
        while(it.hasNext()) {
            if(resource.equals(it.next())) {
                it.remove();
                return;
            }
        }
    }

    public Iterable<StoredResource> listUpdates(int offset, int limit) {
        return limit(skip(updates, offset), limit);
    }

    public static class StoredResource {
        public final long updated;
        public final Resource resource;
        public final String id;

        public StoredResource(long updated, String id, Resource resource) {
            this.id = id;
            this.updated = updated;
            this.resource = resource;
        }

    }
}
