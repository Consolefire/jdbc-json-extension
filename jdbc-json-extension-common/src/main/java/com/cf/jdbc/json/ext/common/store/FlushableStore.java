package com.cf.jdbc.json.ext.common.store;

import java.io.IOException;

public interface FlushableStore {

    void flush() throws IOException;

}
