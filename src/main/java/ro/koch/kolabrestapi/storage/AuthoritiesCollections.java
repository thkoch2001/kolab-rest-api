package ro.koch.kolabrestapi.storage;

import com.google.common.collect.ImmutableSet;

public interface AuthoritiesCollections {

    public String getAuthority();
    public ImmutableSet<Collection> getCollections();
}
