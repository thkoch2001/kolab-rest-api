package ro.koch.kolabrestapi.storage;

import java.util.Map;

import ro.koch.kolabrestapi.models.Collection;
import ro.koch.kolabrestapi.models.Collection.TestCollections;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ConnectedStorage {
    private final Map<String, Collection> collections = Maps.newHashMap();
    private final Map<String, CollectionStorage> collectionStorages = Maps.newHashMap();

    @Inject
    public ConnectedStorage() {
        for(TestCollections collection : TestCollections.values()) {
            add(collection.collection);
        }
    }

    public ImmutableSet<Collection> getCollections() {
        return ImmutableSet.copyOf(collections.values());
    }

    public CollectionStorage getCollectionStorage(String name) {
        return collectionStorages.get(name);
    }

    public Collection getCollection(String name) {
        return collections.get(name);
    }

    private void add(Collection collection) {
        collections.put(collection.getName(), collection);
        collectionStorages.put(collection.getName(), new CollectionStorage());
    }
}
