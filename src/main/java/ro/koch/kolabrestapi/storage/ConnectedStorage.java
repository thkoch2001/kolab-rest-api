package ro.koch.kolabrestapi.storage;

import java.util.Map;
import java.util.Set;

import ro.koch.kolabrestapi.models.Collection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class ConnectedStorage {
    private final Set<Collection> collections = Sets.newHashSet();
    private final Map<String, CollectionStorage> collectionStorages = Maps.newHashMap();

    @Inject
    public ConnectedStorage() {
        add(new Collection("contacts", "Kontakte"));
        add(new Collection("calendar", "Kalender"));
    }

    public ImmutableSet<Collection> getCollections() {
        return ImmutableSet.copyOf(collections);
    }

    public CollectionStorage getCollectionStorage(String name) {
        return collectionStorages.get(name);
    }

    private void add(Collection collection) {
        collections.add(collection);
        collectionStorages.put(collection.getName(), new CollectionStorage());
    }
}
