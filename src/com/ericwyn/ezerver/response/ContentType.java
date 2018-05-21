package com.ericwyn.ezerver.response;

import sun.net.www.content.text.plain;

/**
 *
 * Response 的 ContentType
 *
 * Created by Ericwyn on 18-5-5.
 */
public enum ContentType {
    TEXT_HTML ("text/html "),
    TEXT_CSS ("text/css "),
    IMAGE_PNG ("image/png "),
    IMAGE_JPEG ("image/jpeg "),
    APPLICTION_JSON("application/json "),
    APPLICTION_PDF("application/pdf "),
    APPLICATION_X_JAVASCRIPT ("application/x-javascript "),
    APPLICTION_OCTET_STREAM ("octet-stream "),
    TEXT_PLAIN ("text/plain ");

    private final String type;

    private ContentType(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
