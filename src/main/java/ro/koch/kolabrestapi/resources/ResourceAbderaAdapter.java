package ro.koch.kolabrestapi.resources;

import static org.apache.abdera2.ext.tombstones.TombstonesHelper.DELETED_ENTRY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.AUTHORITY;
import static ro.koch.kolabrestapi.Routes.PathTemplate.COLLECTION;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

import org.apache.abdera2.Abdera;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.ext.history.FeedPagingHelper;
import org.apache.abdera2.ext.tombstones.Tombstone;
import org.apache.abdera2.model.Entry;
import org.apache.abdera2.model.ExtensibleElement;
import org.apache.abdera2.model.Feed;
import org.apache.abdera2.model.Link;

import ro.koch.kolabrestapi.PaginationRange;
import ro.koch.kolabrestapi.Routes.LinkBuilder;
import ro.koch.kolabrestapi.models.Resource;
import ro.koch.kolabrestapi.storage.CollectionStorage.ResultList;
import ro.koch.resourcefacades.TitleAndSummary;

import com.google.inject.Inject;

public class ResourceAbderaAdapter {
    private final Abdera abdera;
    private final LinkBuilder linkBuilder;

    @Inject
    public ResourceAbderaAdapter(Abdera abdera, LinkBuilder linkBuilder) {
        super();
        this.abdera = abdera;
        this.linkBuilder = linkBuilder;
    }

    public Feed buildFeed(PaginationRange range, ResultList resultList) {
        final Feed feed = abdera.newFeed();
        feed.setTitle(linkBuilder.getParam(AUTHORITY) + "'s collection of " + linkBuilder.getParam(COLLECTION));
        ResourceAbderaAdapter adapter = new ResourceAbderaAdapter(abdera, linkBuilder);
        for(final Resource resource : resultList.it) {
            adapter.addResourceToFeed(feed, resource);
        }
        if(range.moreToCome(resultList.total)) {
            FeedPagingHelper.setNext(feed, linkBuilder.next(range).toString());
        }
        return feed;
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

        entry.setUpdated(resource.meta.updated);
        entry.setId(buildId(resource));
        entry.setEdited(resource.meta.updated);
        entry.addExtension(buildEditLink(resource));
        for(Variant variant : resource.availableVariants()) {
            entry.addExtension(buildEditMediaLink(resource, variant.getMediaType()));
        }

        TitleAndSummary titleAndSummary = resource.getFacade(TitleAndSummary.class);
        if(titleAndSummary != null) {
            entry.setTitle(titleAndSummary.getTitle());
            entry.setSummary(titleAndSummary.getSummary());
        } else {
            entry.setTitle("No TitleAndSummary facade found");
        }

        return entry;
    }

    public Tombstone buildTombstone(final Resource resource) {
        Tombstone tombstone = abdera.getFactory().newExtensionElement(DELETED_ENTRY);
        tombstone.setWhen(resource.meta.updated);
        tombstone.setRef(buildId(resource));
        tombstone.addExtension(buildEditLink(resource));
        return tombstone;
    }

    // compare RFC 4122 for the prefix
    public static String buildId(Resource resource) {
        return "urn:uuid:" + resource.meta.id;
    }

    public Link buildEditLink(final Resource resource) {
        Link link = abdera.getFactory().newLink();
        link.setHref(new IRI(linkBuilder.entryUri(resource.meta.id)));
        link.setRel(Link.REL_EDIT);
        return link;
    }

    public Link buildEditMediaLink(final Resource resource, final MediaType mediaType) {
        Link link = abdera.getFactory().newLink();
        link.setHref(new IRI(linkBuilder.mediaEntryUri(resource.meta.id)));
        link.setRel(Link.REL_EDIT_MEDIA);
        link.setMimeType(mediaType.toString());
        return link;
    }
}
