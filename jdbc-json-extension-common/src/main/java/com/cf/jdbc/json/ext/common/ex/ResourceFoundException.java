package com.cf.jdbc.json.ext.common.ex;

public class ResourceFoundException extends RuntimeException {

    private static final long serialVersionUID = 697807403287551474L;

    public ResourceFoundException() {
        super();
    }

    public ResourceFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResourceFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceFoundException(String message) {
        super(message);
    }

    public ResourceFoundException(Throwable cause) {
        super(cause);
    }

}
