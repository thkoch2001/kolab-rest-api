package ro.koch.kolabrestapi.storage;

import java.util.Map;

import com.google.common.collect.Maps;

public class Storages {
    private final Map<String, ConnectedStorage> storages = Maps.newHashMap();

    public ConnectedStorage getForAuthority(String authority) {
        if(!storages.containsKey(authority)) {
            storages.put(authority, new ConnectedStorage());
        }
        return storages.get(authority);
    }

}
