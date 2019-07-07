package com.cf.jdbc.json.ext.common.ex;

public class DuplicateKeyException extends RuntimeException {

    private static final long serialVersionUID = 697807403287551474L;

    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }

}
