package com.ericwyn.ezerver.util;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class ResponseUtil {
    public static String getResponseContentType(String requestUri){

        if (requestUri.toLowerCase().endsWith(".html")){
            return "text/html";
        }else if (requestUri.toLowerCase().endsWith(".js")){
            return "application/x-javascript";
        }else if (requestUri.toLowerCase().endsWith(".css")){
            return "text/css";
        }else if (requestUri.toLowerCase().endsWith(".png")){
            return "image/png";
        }else if (requestUri.toLowerCase().endsWith(".jpg")){
            return "image/jpeg";
        }
        //不知道是什么的时候直接返回流
        else {
            return "application/octet-stream";
        }
    }


}
