package ro.koch.resourcefacades;

import java.io.OutputStream;

public interface Writer {
    void writeTo(OutputStream entityStream);
}
