package com.ericwyn.ezerver.request;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class RequestParam {
    private String key;
    private String value;
    private String ContentDisposition;

    public RequestParam() {
    }

    public RequestParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContentDisposition() {
        return ContentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        ContentDisposition = contentDisposition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
