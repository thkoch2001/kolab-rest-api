package ro.koch.kolabrestapi.resources;

import static org.apache.abdera2.ext.tombstones.TombstonesHelper.DELETED_ENTRY;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.ext.tombstones.Tombstone;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.ExtensibleElement;
import org.apache.abdera2.model.Feed;
import org.joda.time.DateTime;

import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.models.Resource;

public class ResourceAbderaAdapter {
    private final Abdera abdera;
    private final LinkBuilder linkBuilder;
    private final String collection;

    public ResourceAbderaAdapter(Abdera abdera, LinkBuilder linkBuilder, String collection) {
        super();
        this.abdera = abdera;
        this.linkBuilder = linkBuilder;
        this.collection = collection;
    }


    public void addResourceToFeed(final Feed feed, final Resource resource) {
        feed.addExtension(buildFeedElement(resource));
    }

    public ExtensibleElement buildFeedElement(final Resource resource) {
        if(resource.isDeleted()) {
            return buildTombstone(resource);
        } else {
            return buildEntry(resource);
        }
    }

    public Entry buildEntry(final Resource resource) {
        final Entry entry = abdera.newEntry();
        //entry.addAuthor(item.getAuthor());
        //entry.setTitle(item.getTitle());
        entry.setUpdated(new DateTime(resource.meta.updated));
        entry.setId(resource.meta.id);
        entry.setEdited(new DateTime(resource.meta.updated));
        entry.addLink(new IRI(linkBuilder.mediaEntryUri(collection, resource.meta.id)),
                      "edit-media",
                      resource.mediaType.toString(),
                      null, null, -1l);
        entry.addLink(new IRI(linkBuilder.entryUri(collection, resource.meta.id)), "edit");
        //entry.setSummary(item.getSummary());
        return entry;
    }

    public Tombstone buildTombstone(final Resource resource) {
        Tombstone tombstone = abdera.getFactory().newExtensionElement(DELETED_ENTRY);
        tombstone.setWhen(resource.meta.updated);
        tombstone.setRef(resource.meta.id);
        return tombstone;
    }
}
