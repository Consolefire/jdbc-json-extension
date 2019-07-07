package com.cf.jdbc.json.ext.common.store;

import java.io.Closeable;
import java.io.IOException;

public interface CloseableStore extends Closeable {

    @Override
    default void close() throws IOException {
        if (this instanceof FlushableStore) {
            ((FlushableStore) this).flush();
        }
        doClose();
    }

    void doClose() throws IOException;

}
