package com.cf.jdbc.json.ext.common.ex;

public class IllegalConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -1916618460277544999L;

    public IllegalConfigurationException() {}

    public IllegalConfigurationException(String message) {
        super(message);
    }

    public IllegalConfigurationException(Throwable cause) {
        super(cause);
    }

    public IllegalConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalConfigurationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
