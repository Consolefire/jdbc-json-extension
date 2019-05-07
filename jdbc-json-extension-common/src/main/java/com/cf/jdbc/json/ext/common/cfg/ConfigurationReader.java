package com.cf.jdbc.json.ext.common.cfg;

import java.io.IOException;
import java.io.Serializable;

public interface ConfigurationReader<S extends Serializable> {

    S read() throws IOException;

}
