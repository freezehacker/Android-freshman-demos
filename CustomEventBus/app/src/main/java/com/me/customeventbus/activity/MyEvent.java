package com.me.customeventbus.activity;

/**
 * Created by sjk on 17-6-9.
 */

public class MyEvent {

    private int code;
    private String message;

    public MyEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
