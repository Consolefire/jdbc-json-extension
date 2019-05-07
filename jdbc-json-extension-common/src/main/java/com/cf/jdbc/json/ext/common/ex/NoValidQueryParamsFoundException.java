package com.cf.jdbc.json.ext.common.ex;

public class NoValidQueryParamsFoundException extends RuntimeException {

    private static final long serialVersionUID = 697807403287551474L;

    public NoValidQueryParamsFoundException() {
        super();
    }

    public NoValidQueryParamsFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoValidQueryParamsFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValidQueryParamsFoundException(String message) {
        super(message);
    }

    public NoValidQueryParamsFoundException(Throwable cause) {
        super(cause);
    }

}
