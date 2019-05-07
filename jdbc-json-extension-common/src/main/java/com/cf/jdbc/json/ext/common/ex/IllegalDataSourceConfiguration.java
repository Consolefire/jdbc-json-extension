package com.cf.jdbc.json.ext.common.ex;

public class IllegalDataSourceConfiguration extends RuntimeException {

    private static final long serialVersionUID = -1916618460277544999L;

    public IllegalDataSourceConfiguration() {}

    public IllegalDataSourceConfiguration(String message) {
        super(message);
    }

    public IllegalDataSourceConfiguration(Throwable cause) {
        super(cause);
    }

    public IllegalDataSourceConfiguration(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDataSourceConfiguration(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
