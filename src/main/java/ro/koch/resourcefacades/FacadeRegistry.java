package ro.koch.resourcefacades;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class FacadeRegistry {
    ImmutableMultimap<Class<? extends Object>, FacadeFactory<?>> registry;

    public FacadeRegistry(Multimap<Class<? extends Object>, FacadeFactory<?>> registry) {
        this.registry = ImmutableListMultimap.copyOf(registry);
    }

    /**
     * Returns Facade factories that could probably build the requested Facade.
     *
     * @param mediaType MediaType of the original Resource
     * @param clazz requested Facade interface
     */
    public Iterable<FacadeFactory<?>> getFacadeFactories(Class<? extends Object> clazz) {
        return registry.get(clazz);
    }

    public static class Builder {
        Multimap<Class<? extends Object>, FacadeFactory<?>> registry = ArrayListMultimap.create();

        public <T> Builder put(Class<T> clazz, FacadeFactory<? extends T> factory) {
            registry.put(clazz, factory);
            return this;
        }

        public FacadeRegistry build() {
            return new FacadeRegistry(registry);
        }
    }
}
