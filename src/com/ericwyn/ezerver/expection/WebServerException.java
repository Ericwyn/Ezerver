package com.ericwyn.ezerver.expection;

/**
 * Created by Ericwyn on 18-5-1.
 */
public class WebServerException extends Exception{
    private String msg;
    private int code;

    public WebServerException(){
        super();
    }

    public WebServerException(String msg){
        this.msg = msg;
        this.code = -1;
    }

    public WebServerException(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

}
