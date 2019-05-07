package com.cf.jdbc.json.ext.core.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.cf.jdbc.json.ext.common.cfg.AbstractConfigurationReader;

public abstract class FileSystemConfigurationReader extends AbstractConfigurationReader<String> {

    protected final String configLocation;

    public FileSystemConfigurationReader(String configLocation) {
        this.configLocation = configLocation;
    }

    protected abstract String getFilePath() throws IOException;

    @Override
    public String read() throws IOException {
        String filePath = getFilePath();
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Config file Not Found. Location [ " + configLocation + " ]");
        }
        byte[] buffer = Files.readAllBytes(Paths.get(filePath));
        if (null != buffer && buffer.length > 0) {
            return new String(buffer);
        }
        throw new IOException("Failed to read Config file [ " + configLocation + " ]");
    }


}
