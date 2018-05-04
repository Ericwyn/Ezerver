package com.ericwyn.ezerver.response;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.util.LogUtils;
import com.ericwyn.ezerver.util.ResponseUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * Created by Ericwyn on 18-5-2.
 */
public class Response {
    private Request requset;
    private static final int BUFFER_SIZE = 1024;
    OutputStream output ;

    public Response(Request request,OutputStream output){
        this.requset = request;
        this.output = output;
    }

    public void setRequset(Request requset) {
        this.requset = requset;
    }

    public void sendStaticResource() throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            //通过 uri 获取文件地址
            if (requset==null){
                output.write(ErrorMsg.ERR_404_MSG.getBytes());
            }else {
                String fileName = requset.getUri().split("\\?")[0];
                File file = new File(SimpleHttpServer.WEB_ROOT,fileName);
                //将 uri 文件内容放到返回的 http 包里面
                if (file.isFile()){
                    output.write("HTTP/1.1 200 OK\n".getBytes());
                    String contentType = ResponseUtil.getResponseContentType(fileName);
                    output.write(("Content-Type: "+contentType+"; charset=UTF-8\n\n").getBytes());
                    fis = new FileInputStream(file);
                    int readLength;
                    while ((readLength = fis.read(buffer,0,BUFFER_SIZE))>0){
                        output.write(buffer,0,readLength);
                    }
                }else {
                    output.write(ErrorMsg.ERR_404_MSG.getBytes());
                }
            }

        }catch (SocketException e){
            LogUtils.errorLoger("连接发生异常，可能是连接已经断开：异常信息如下："+e.getMessage());
        }
        finally {
            if (fis!=null){
                fis.close();
            }
        }

    }

}
