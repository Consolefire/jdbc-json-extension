package com.cf.jdbc.json.ext.common.ex;

public class NoResultFoundException extends RuntimeException {

    private static final long serialVersionUID = 697807403287551474L;

    public NoResultFoundException() {
        super();
    }

    public NoResultFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoResultFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResultFoundException(String message) {
        super(message);
    }

    public NoResultFoundException(Throwable cause) {
        super(cause);
    }

}
