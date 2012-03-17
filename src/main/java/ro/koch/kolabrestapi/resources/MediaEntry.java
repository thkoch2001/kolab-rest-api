package ro.koch.kolabrestapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

public class MediaEntry {
    @GET
    public Response get(@InjectParam EntryGetCommon getCommon) {
        return getCommon.getMediaEntryRespone();
    }
}
