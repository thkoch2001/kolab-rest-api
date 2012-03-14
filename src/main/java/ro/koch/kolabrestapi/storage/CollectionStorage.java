package ro.koch.kolabrestapi.storage;

import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.skip;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.EntityTag;

import org.joda.time.DateTime;

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

    public Resource conditionalGet(String id, EntityTag etag) {
        return resourcesMap.get(id);
    }

    public void conditionalPut(String id, Resource resource, DateTime timestamp, EntityTag etag) {
        removeOlderEntryFromUpdatesList(resourcesMap.get(id));
        Resource newResource = resource.update(resource, timestamp);
        resourcesMap.put(id, resource);
        updates.add(0, newResource);
    }

    public void conditionalDelete(String id, DateTime dateTime, EntityTag etag) {
        Resource oldResource = resourcesMap.get(id);
        removeOlderEntryFromUpdatesList(oldResource);
        Resource newResource = oldResource.delete(dateTime);
        updates.add(0, newResource);
        resourcesMap.put(id, newResource);
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

    public Iterable<Resource> listUpdates(int offset, int limit) {
        return limit(skip(updates, offset), limit);
    }
}
