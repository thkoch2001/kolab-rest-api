package ro.koch.resourcefacades;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class TrivialFacadeFactory<T> implements FacadeFactory<T> {
    protected final Class<T> classToBuild;
    private final Iterable<? extends Class<? extends Object>> dependentClasses;
    private final Constructor<T> ctor;

    public TrivialFacadeFactory(Class<T> classToBuild,
                                Iterable<? extends Class<? extends Object>> dependentClasses) {
        this.classToBuild = checkNotNull(classToBuild);
        this.dependentClasses = dependentClasses;
        try {
            this.ctor = classToBuild.getConstructor(getFirst());
        } catch (SecurityException e) {
            throw propagate(e);
        } catch (NoSuchMethodException e) {
            throw propagate(e);
        }
    }

    public static <T> TrivialFacadeFactory<T> of(Class<T> classToBuild, Class<?> dependentClass) {
        return new TrivialFacadeFactory<T>(classToBuild, ImmutableSet.of(dependentClass));
    }

    @Override
    public T build(FacadesProvider resourceHandler) {
        try {
            return ctor.newInstance(resourceHandler.getFacade(getFirst()));
        } catch (IllegalArgumentException e) {
            throw propagate(e);
        } catch (InstantiationException e) {
            throw propagate(e);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        } catch (InvocationTargetException e) {
            throw propagate(e);
        }
    }

    private Class<?> getFirst() {
        return Iterables.getFirst(dependentClasses, null);
    }

    @Override
    public Iterable<? extends Class<? extends Object>> getDependencies() {
        return dependentClasses;
    }

    @Override
    public boolean checkPredicate(Predicate<FacadeFactory<?>> predicate) {
        return true;
    }
}
