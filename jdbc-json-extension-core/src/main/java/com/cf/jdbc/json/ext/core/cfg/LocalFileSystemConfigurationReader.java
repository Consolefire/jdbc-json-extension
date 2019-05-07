package com.cf.jdbc.json.ext.core.cfg;

import java.io.IOException;

public class LocalFileSystemConfigurationReader extends FileSystemConfigurationReader {

    public LocalFileSystemConfigurationReader(String configLocation) {
        super(configLocation);
    }

    @Override
    protected String getFilePath() throws IOException {
        return configLocation;
    }

}
