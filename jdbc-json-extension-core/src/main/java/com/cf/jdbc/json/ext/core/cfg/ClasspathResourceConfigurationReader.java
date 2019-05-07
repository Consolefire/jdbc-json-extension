package com.cf.jdbc.json.ext.core.cfg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ClasspathResourceConfigurationReader extends FileSystemConfigurationReader {

    public ClasspathResourceConfigurationReader(String configLocation) {
        super(configLocation);
    }

    @Override
    protected String getFilePath() throws IOException {
        URL resource = getClass().getResource(configLocation);
        if (null == resource) {
            throw new FileNotFoundException("Failed to get classpath config file: [ " + configLocation + " ]");
        }
        try {
            return resource.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

}
