package com.ericwyn.ezerver.util;

import com.ericwyn.ezerver.response.ContentType;

/**
 *
 *
 *
 * Created by Ericwyn on 18-5-4.
 */
public class ResponseUtil {
    /**
     * 通过请求头的 URI 信息，或者文件名来匹配 Response 的 ContentType 类型
     * @param requestUri
     * @return
     */
    public static ContentType getResponseContentType(String requestUri){
        if (requestUri.toLowerCase().endsWith(".html")){
            return ContentType.TEXT_HTML;
        }else if (requestUri.toLowerCase().endsWith(".js")){
            return ContentType.APPLICATION_X_JAVASCRIPT;
        }else if (requestUri.toLowerCase().endsWith(".css")){
            return ContentType.TEXT_CSS;
        }else if (requestUri.toLowerCase().endsWith(".png")){
            return ContentType.IMAGE_PNG;
        }else if (requestUri.toLowerCase().endsWith(".jpg")){
            return ContentType.IMAGE_JPEG;
        }else if (requestUri.toLowerCase().endsWith(".json")){
            return ContentType.APPLICTION_JSON;
        }else if (requestUri.toLowerCase().endsWith(".pdf")){
            return ContentType.APPLICTION_PDF;
        }
        //不知道是什么的时候直接返回流
        else {
            return ContentType.APPLICTION_OCTET_STREAM;
        }
    }


}
