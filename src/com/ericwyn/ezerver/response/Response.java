package com.ericwyn.ezerver.response;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.util.LogUtils;
import com.ericwyn.ezerver.util.ResponseUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

/**
 *
 * 新建方法，需要 Request 和 OutputStream
 *      通过 需要 Request 和 OutputStream，Request 读取请求内容，OutputStream 输出请求内容
 *
 * sendStaticResource() 直接以静态文件请求来处理，就是调用下面的方法来完成的
 *
 * sendResponseLine( StatueCode ) 发送请求行
 *
 * sendResponseHeader( ContentType ) 发送请求体
 *      设置 contentType
 *
 * sendResponseStream() 发送请求具体内容
 *
 * 关闭方法
 *      关闭 Request 里的 BufferReader 和 OutputStream 的流
 *
 * Created by Ericwyn on 18-5-2.
 */
public class Response {

    private static final int BUFFER_SIZE = 2048;
    private static LogUtils logUtils = SimpleHttpServer.logUtils;

    private Request requset;
    private OutputStream output ;

    public Response(Request request) throws IOException {
        this.requset = request;
        this.output = request.getSocket().getOutputStream();
    }

    public void sendStaticResource() throws IOException{
        try {
            //通过 uri 获取文件地址
            if (requset==null){
                output.write(ErrorMsg.ERR_404_MSG.getBytes());
            }else {
                String fileName = requset.getUri().split("\\?")[0];
                File file = new File(SimpleHttpServer.WEB_ROOT,fileName);
                //将 uri 文件内容放到返回的 http 包里面
                if (file.isFile()){
                    // 输出头部响应行
                    sendResponseLine(StateCode.CODE_200);
                    ContentType contentType = ResponseUtil.getResponseContentType(fileName);
                    //输出content-type
                    sendResponseHeader(contentType);
                    //输出文件信息
                    sendResponseBody(file);
                }else {
                    sendResponseLine(StateCode.CODE_404);
                    sendResponseHeader(ContentType.TEXT_HTML);
                    sendResponseBody(ErrorMsg.ERR_404_MSG);
                }

            }

        }catch (SocketException e){
            logUtils.errorLoger("连接发生异常，可能是连接已经断开：异常信息如下："+e.getMessage());
        }
    }

    private void sendResponseLine(StateCode stateCode) throws IOException {
        output.write(("HTTP/1.1 "+stateCode).getBytes());
    }

    private void sendResponseHeader(ContentType contentType) throws IOException {
        output.write(("Content-Type: "+contentType+"; charset=UTF-8\n\n").getBytes());
    }

    public OutputStream getOutputStream() {
        return output;
    }

    //发送文件形式的 ResponseBody
    //不判断文件是否存在
    private void sendResponseBody(File file) throws IOException {
//        FileInputStream fis = new FileInputStream(file);
//        byte[] buffer = new byte[fis.available()];
//        int readLength;
//        while ((readLength = fis.read(buffer,0,BUFFER_SIZE))>0){
//            output.write(buffer,0,readLength);
//            output.flush();
//        }
//        output.write(buffer,0,readLength);
        //临时的文件读取流要在这里关闭
//        fis.close();

//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//        byte[] bytes = new byte[bis.available()];
//        int read = bis.read(bytes);
//        output.write(bytes,0,read);
//        bis.close();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[2048];
        int read;
        while ((read=bis.read(bytes))!=-1){
            output.write(bytes,0,read);
            output.flush();
        }
        bis.close();
    }

    //直接发送文字形式的 ResponseBody
    private void sendResponseBody(String bodyStr) throws IOException {
        output.write(bodyStr.getBytes());
    }

    //用户返回 json 数据
    public void sendJsonData(String json) throws IOException {
        sendResponseLine(StateCode.CODE_200);
        sendResponseHeader(ContentType.APPLICTION_JSON);
        sendResponseBody(json);
    }

    //用户返回文件下载
    //文件不处在时候返回404
    public void sendFileStream(File file) throws IOException {
        sendResponseLine(StateCode.CODE_200);
        sendResponseHeader(ContentType.APPLICTION_OCTET_STREAM);
        if (file.exists()){
            logUtils.debugLoger("请求文件： "+file.getAbsolutePath()+" 存在");
            sendResponseBody(file);
        }else {
            sendResponseBody(ErrorMsg.ERR_404_MSG);
        }
    }

    public void closeStream() throws IOException {
        this.requset.getBufferReader().close();
        this.getOutputStream().close();
        this.requset.getSocket().close();
        this.requset = null;
        logUtils.debugLoger("关闭了 inputStream 和 outputStream 和请求的 Socket 连接");
    }
}
