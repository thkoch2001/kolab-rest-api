package ro.koch.kolabrestapi;

import static com.google.common.collect.Iterables.getFirst;
import static javax.ws.rs.core.HttpHeaders.IF_MATCH;
import static javax.ws.rs.core.HttpHeaders.IF_MODIFIED_SINCE;
import static javax.ws.rs.core.HttpHeaders.IF_NONE_MATCH;
import static javax.ws.rs.core.HttpHeaders.IF_UNMODIFIED_SINCE;

import java.util.List;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;


public abstract class Preconditions {
    protected static final String WILDCARD = "\"*\"";

    protected final String ifNoneMatch;

    public Preconditions(HttpHeaders headers) {
        ifNoneMatch = getHeader(headers, IF_NONE_MATCH);
    }

    public abstract boolean shouldPerform(EntityTag etag);
    // shouldPerform for Date, etag+date

    /**
     * TODO this is naive:
     * - there may be multiple lines of IF-* headers
     * - one line might contain multiple values seperated by ,
     * - the etag values could be encapsulated in ""
     */
    protected static boolean matches(EntityTag etag, String header) {
        return WILDCARD.equals(header)
               || etag.equals(EntityTag.valueOf(header));
    }

    protected static String getHeader(HttpHeaders headers, String name) {
        List<String> headersForName = headers.getRequestHeader(name);
        return null == headersForName ? null
               : getFirst(headersForName, null);
    }

    public static class GetHeadPreconditions extends Preconditions {
        private final String ifModifiedSince;

        public GetHeadPreconditions(HttpHeaders headers) {
            super(headers);
            ifModifiedSince = getHeader(headers, IF_MODIFIED_SINCE);
        }

        @Override public boolean shouldPerform(EntityTag etag) {
            return ifNoneMatch == null || !matches(etag, ifNoneMatch);
        }
    }

    public static class PutDeletePreconditions extends Preconditions  {
        private final String ifMatch;
        private final String ifUnmodifiedSince;

        public PutDeletePreconditions(HttpHeaders headers) {
            super(headers);
            ifMatch = getHeader(headers, IF_MATCH);
            ifUnmodifiedSince = getHeader(headers, IF_UNMODIFIED_SINCE);
        }

        @Override public boolean shouldPerform(EntityTag etag) {
            return ifMatch == null || matches(etag, ifMatch);
        }
    }
}
