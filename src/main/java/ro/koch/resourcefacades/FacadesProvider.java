package ro.koch.resourcefacades;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import com.google.common.base.Predicate;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

public class FacadesProvider {
    private final FacadeRegistry facadeRegistry;
    private final MutableClassToInstanceMap<Object> facades = MutableClassToInstanceMap.create();

    public FacadesProvider(FacadeRegistry facadeRegistry, ClassToInstanceMap<Object> inputFacades) {
        this.facadeRegistry = checkNotNull(facadeRegistry);
        for(Entry<Class<? extends Object>, ? extends Object> entry: inputFacades.entrySet()) {
            facades.put(entry.getKey(), checkNotNull(entry.getValue()));
        }
        checkArgument(!facades.isEmpty(),"No inputFacades provided!");
    }

    public FacadesProvider(FacadeRegistry facadeRegistry, MediaType mediaType, Class<Object> clazz, Object facade) {
        this(facadeRegistry, ImmutableClassToInstanceMap.builder().put(clazz, facade).build());
    }

    /**
     * Returns the unique instance of a Facade for this Resource
     *
     * Subsequent calls with the same parameter receive the _same_ unique Facade instance!
     *
     * @param clazz requested Facade interface
     * @return Facade implementation instance
     */
    public <T> T getFacade(Class<T> clazz) {
        if(!facades.containsKey(clazz)) {
            FacadeFactory<?> factory = resolveFacadeFactory(clazz);
            // check factory type
            // check for null
            facades.put(checkNotNull(clazz), factory.build(this));
        }
        return facades.getInstance(clazz);
    }

    public <T> T getFacade(Class<T> clazz, Predicate<FacadeFactory<?>> predicate) {
        FacadeFactory<? extends T> factory = resolveFacadeFactory(clazz, predicate);
        if(null == factory) return null;
        return factory.build(this);
    }

    private <T> FacadeFactory<? extends T> resolveFacadeFactory(Class<T> clazz) {
        return resolveFacadeFactory(clazz, null);
    }

    private <T> FacadeFactory<? extends T> resolveFacadeFactory(Class<T> clazz,
                                             Predicate<FacadeFactory<?>> predicate) {
        for(FacadeFactory<?> factory : facadeRegistry.getFacadeFactories(clazz)) {
            // TODO check generic type
            FacadeFactory<? extends T> castedFactory = (FacadeFactory<? extends T>) factory;
            if(checkDependencies(castedFactory)
               && (predicate == null || castedFactory.checkPredicate(predicate))) {
                return castedFactory;
            }
        }
        return null;
    }

    private boolean checkDependencies(FacadeFactory<?> factory) {
        for(Class<?> dependency : factory.getDependencies()) {
            if(facades.containsKey(dependency)) continue;
            if(!hasFacade(dependency)) return false;
        }
        return true;
    }

    /**
     * Is the requested Facade interface available for this Resource?
     *
     * @param clazz Facade interface
     */
    public boolean hasFacade(Class<?> clazz) {
        if(facades.containsKey(clazz)) return true;
        return null != resolveFacadeFactory(clazz);
    }
}