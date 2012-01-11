package ro.koch.kolabrestapi.resources;

import javax.ws.rs.PathParam;

public class Authority {

    private final String authority;

    public Authority(@PathParam("version") String authority) {
        this.authority = authority;
    }

}
