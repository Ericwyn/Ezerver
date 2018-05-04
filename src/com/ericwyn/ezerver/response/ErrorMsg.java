package com.ericwyn.ezerver.response;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class ErrorMsg {
    public static String ERR_404_MSG =
            "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" + "\r\n" +
                    "<h1>File Not Found</h1>";

    //application/json

    public static String ERROR_JSON_MSG =
            "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" + "\r\n" +
                    "<h1>File Not Found</h1>";

}
