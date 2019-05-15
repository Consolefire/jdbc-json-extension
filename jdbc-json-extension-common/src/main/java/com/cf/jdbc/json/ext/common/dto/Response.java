package com.cf.jdbc.json.ext.common.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    public enum Status {
        SUCCESS, FAILED
    }

    private final T body;
    private final Status status;
    private List<String> errors;

    public Response(T body, Status status) {
        this.body = body;
        this.status = status;
    }

    public boolean isEmpty() {
        return null == body;
    }

    public boolean isSuccess() {
        return null != status && Status.SUCCESS.equals(status);
    }

    public static <R> Response<R> empty() {
        return new Response<R>(null, Status.SUCCESS);
    }

    public static <R> Response<R> with(R body, Status status) {
        return new Response<R>(body, status);
    }

    public static <R> Response<R> success(R body) {
        return new Response<R>(body, Status.SUCCESS);
    }

    public static <R> Response<R> error() {
        return new Response<R>(null, Status.FAILED);
    }

    public static <R> Response<R> error(String... errors) {
        Response<R> r = new Response<>(null, Status.FAILED);
        if (null != errors && errors.length > 0) {
            r.errors = Arrays.stream(errors).collect(Collectors.toList());
        }
        return r;
    }

    public static <R> Response<R> error(List<String> errors) {
        Response<R> r = new Response<>(null, Status.FAILED);
        if (null != errors && !errors.isEmpty()) {
            r.errors = errors;
        }
        return r;
    }

}
