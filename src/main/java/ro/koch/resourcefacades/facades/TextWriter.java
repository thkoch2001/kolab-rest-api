package ro.koch.resourcefacades.facades;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.OutputSupplier;

public class TextWriter extends AbstractWriter<String> {

    public TextWriter(String object) {
        super(object);
    }

    @Override
    public void writeTo(final OutputStream out) {
        try {
            ByteStreams.write(object.getBytes(), new OutputSupplier<OutputStream>(){
                @Override public OutputStream getOutput() throws IOException {return out;}
              }
            );
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

}
