package com.ericwyn.ezerver.handle;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParam;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 自定义的处理方法
 *
 * Created by Ericwyn on 18-5-4.
 */
public abstract class HandleMethod {
    private String uri;
    public HandleMethod(String uri){
        this.uri=uri;
    }

    //回调接口，outputStream  是回调的东西
    // 参数 1 是 Request ， 参数 2 是outputStream，输出流
    public void RequestDo(Request request, Response response) throws IOException {

    }

    public String getUri() {
        return uri;
    }
}
