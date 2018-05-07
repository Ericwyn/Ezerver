package com.ericwyn.ezerver.handle;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;

/**
 *
 * uri 和 matchRegex 参数并不在内部调用，当初的只是给上一层的 SimpleHttpServer 判断是都将 Request 转发到 requestDo 方法而已
 *
 * 自定义的处理方法
 *
 * Created by Ericwyn on 18-5-4.
 */
public abstract class HandleMethod {
    // 存储该方法绑定的 uri
    private String uri;

    //是否使用 正则表达式匹配 uri
    private boolean matchRegex;

    public HandleMethod(String uri){
        this(uri,false);
    }

    public HandleMethod(String uri,boolean matchRegex){
        this.uri=uri;
        this.matchRegex = matchRegex;
    }

    //回调接口，outputStream  是回调的东西
    // 参数 1 是 Request ， 参数 2 是outputStream，输出流
    public void requestDo(Request request, Response response) throws IOException {

    }

    public String getUri() {
        return uri;
    }

    public boolean isMatchRegex() {
        return matchRegex;
    }
}
