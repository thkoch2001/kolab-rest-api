package ro.koch.resourcefacades;

import com.google.common.base.Predicate;

public interface FacadeFactory<T> {
    public T build(FacadesProvider resourceHandler);

    /**
     * Dependency Facades needed by this factory.
     */
    public Iterable<? extends Class<? extends Object>> getDependencies();

    public boolean checkPredicate(Predicate<FacadeFactory<?>> predicate);
}
