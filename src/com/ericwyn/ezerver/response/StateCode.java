package com.ericwyn.ezerver.response;

/**
 * 响应头的状态码
 *
 * Created by Ericwyn on 18-5-5.
 */
public enum StateCode {
    CODE_404 ("404 File Not Found "),
    CODE_200 ("200 OK ");

    private final String code;

    private StateCode(String code){
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
