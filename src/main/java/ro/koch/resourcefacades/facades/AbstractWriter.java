package ro.koch.resourcefacades.facades;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.contains;

import javax.ws.rs.core.MediaType;

import ro.koch.resourcefacades.FacadeFactory;
import ro.koch.resourcefacades.TrivialFacadeFactory;
import ro.koch.resourcefacades.Writer;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

public abstract class AbstractWriter<T> implements Writer {
    protected final T object;

    public AbstractWriter(T object) {
        this.object = checkNotNull(object);
    }

    public static class WriterFactory<T extends Writer> extends TrivialFacadeFactory<T> {
        private final Iterable<MediaType> mediaTypes;

        public WriterFactory(Class<T> classToBuild,
                             Class<? extends Object> dependentClass,
                             Iterable<MediaType> mediaTypes) {
            super(classToBuild, ImmutableSet.of(dependentClass));
            this.mediaTypes = mediaTypes;
        }

        public boolean isWriteable(MediaType mediaType) {
            return contains(mediaTypes, mediaType);
        }

        @Override public boolean checkPredicate(Predicate<FacadeFactory<?>> predicate) {
            return predicate.apply(this);
        }
    }
}
