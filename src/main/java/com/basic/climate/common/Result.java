package com.basic.climate.common;

import lombok.Data;

@Data
public class Result<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setMessage("OK");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
