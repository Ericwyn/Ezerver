package com.ericwyn.ezerver.request;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class RequestParam {
    String key;
    String value;

    public RequestParam() {
    }

    public RequestParam(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
