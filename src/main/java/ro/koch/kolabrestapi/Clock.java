package ro.koch.kolabrestapi;

import org.joda.time.DateTime;

import com.google.common.base.Supplier;
import com.google.inject.Singleton;

@Singleton
public class Clock implements Supplier<DateTime>{

    @Override public DateTime get() {
        return DateTime.now();
    }
}
