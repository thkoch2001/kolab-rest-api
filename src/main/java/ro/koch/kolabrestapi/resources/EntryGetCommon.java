package ro.koch.kolabrestapi.resources;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML_TYPE;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;
import static ro.koch.kolabrestapi.MediaTypes.APPLICATION_ATOMDELETED_XML_TYPE;
import static ro.koch.kolabrestapi.Routes.PathTemplate.ENTRY;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;

import ro.koch.kolabrestapi.Preconditions;
import ro.koch.kolabrestapi.Routes.PathParams;
import ro.koch.kolabrestapi.storage.CollectionStorage;
import ro.koch.kolabrestapi.storage.CollectionStorage.GetResult;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class EntryGetCommon {

    public final CollectionStorage storage;
    public final PathParams pathParams;
    public final Preconditions preconditions;
    public final Request request;

    @Inject
    public EntryGetCommon(CollectionStorage storage, PathParams pathParams,
                          Preconditions preconditions, Request request) {
        this.storage = checkNotNull(storage);
        this.pathParams = checkNotNull(pathParams);
        this.preconditions = checkNotNull(preconditions);
        this.request = checkNotNull(request);
    }

    public GetResult getResourceResult() {
        return storage.conditionalGet(pathParams.get(ENTRY), preconditions);
    }

    private Response buildResponse(OkFkt okFkt) {
        GetResult getResult = getResourceResult();
        ResponseBuilder rb = Response.status(getResult.status);
        if(getResult.status == Status.OK) okFkt.ok(getResult, rb);
        return rb.build();
    }

    public Response getMediaEntryRespone() {
        return buildResponse(new OkFkt() {@Override public void ok(GetResult gr, ResponseBuilder rb) {
           Variant variant = gr.resource.selectVariant(request);
           rb
             .entity(gr.resource)
//           .entity(gr.resource.asVariant(variant))
           .tag(gr.resource.meta.getETag())
           .variant(variant)
           .variants(gr.resource.availableVariants());
        }});
    }

    public Response getEntryResponse(final ResourceAbderaAdapter abderaAdapter) {
        return buildResponse(new OkFkt() {@Override public void ok(GetResult gr, ResponseBuilder rb) {
           rb
            .tag(gr.resource.meta.getETag())
            .type(gr.resource.isDeleted()
                   ? APPLICATION_ATOMDELETED_XML_TYPE
                   : APPLICATION_ATOM_XML_TYPE)
            .entity(abderaAdapter.buildFeedElement(gr.resource));
         }});
    }

    static Response condResponse(boolean conditionOk) {
        return Response.status(conditionOk ? NO_CONTENT : PRECONDITION_FAILED).build();
    }

    private interface OkFkt {
        void ok(GetResult getResult, ResponseBuilder rb);
    }
}
