package com.ericwyn.ezerver.response;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.SimpleHttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
            File file = new File(SimpleHttpServer.WEB_ROOT,requset.getUri());
            //将 uri 文件内容放到返回的 http 包里面
            if (file.exists()){
                output.write("HTTP/1.1 200 OK\n".getBytes());
                if (requset.getUri().endsWith(".css")){

                }else if (requset.getUri().endsWith(".css")){

                }
                output.write("Content-Type: text/html; charset=UTF-8\n\n".getBytes());
                fis = new FileInputStream(file);
                int readLength;
                while ((readLength = fis.read(buffer,0,BUFFER_SIZE))>0){
                    output.write(buffer,0,readLength);
                }
            }else {
                output.write(ErrorMsg.ERR_404_MSG.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (fis!=null){
                fis.close();
            }
        }

    }

}
