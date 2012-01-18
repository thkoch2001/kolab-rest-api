package ro.koch.kolabrestapi.storage;

import java.util.Map.Entry;

import net.fortuna.ical4j.vcard.Property.Id;
import net.fortuna.ical4j.vcard.VCard;
import ro.koch.kolabrestapi.models.VCardListItem;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

public class ConnectedStorage {
    private final Table<String, String, VCard> collections = HashBasedTable.create();

    public ImmutableSet<String> getCollections() {
        return ImmutableSet.copyOf(collections.rowKeySet());
    }

    public ImmutableList<VCardListItem> getItems(String collection, int offset, int limit) {
        final ImmutableList.Builder<VCardListItem> list = ImmutableList.builder();
        for(final Entry<String, VCard> item : collections.row(collection).entrySet()) {
            list.add(new VCardListItem(item.getValue()));
        }
        return list.build();
    }

    public String createItem(String collection, VCard item) {
        final String id = item.getProperty(Id.UID).getValue();
        collections.put(collection, id, item);
        return id;
    }

    public VCard getItem(String collection, String id) {
        return collections.get(collection, id);
    }

    public void updateItem(String collection, String id, VCard item) {
        collections.put(collection, id, item);
    }

    public void deleteItem(String collection, String id) {
        collections.remove(collection, id);
    }
}
