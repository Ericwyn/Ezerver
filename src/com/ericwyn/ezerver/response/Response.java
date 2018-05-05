package com.ericwyn.ezerver.response;

import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.util.LogUtil;
import com.ericwyn.ezerver.util.ResponseUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private static LogUtil sLogUtil = SimpleHttpServer.logUtil;

    private Request requset;
    private OutputStream output ;

    public Response(Request request) throws IOException {
        this.requset = request;
        this.output = request.getSocket().getOutputStream();
    }

    private OutputStream getOutputStream() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    /**
     * sendStaticResource 是默认的对静态资源 GET 请求的处理方法
     *
     * 包括其他的一些没有设定的特殊的请求，也将使用这个方法自动处理
     * 如果资源不存在的话将会返回 404 页面
     *
     * @throws IOException
     */
    public void sendStaticResource() throws IOException{
        try {
            //通过 uri 获取文件地址
            if (requset==null){
                sendResponseLine(StateCode.CODE_404);
                sendResponseHeader(ContentType.TEXT_HTML);
                sendResponseBody(ErrorMsg.ERR_404_MSG);
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
            sLogUtil.errorLoger("连接发生异常，可能是连接已经断开：异常信息如下："+e.getMessage());
        }
    }

    /**
     * 内部调用方法
     *
     * 发送响应报文的 响应行
     *
     * 格式类似于是
     *
     *      HTTP/1.1 200 OK
     *
     * @param stateCode
     * @throws IOException
     */
    private void sendResponseLine(StateCode stateCode) throws IOException {
        output.write(("HTTP/1.1 "+stateCode).getBytes());
    }

    /**
     * 内部调用方法
     *
     * 发送响应报文的 响应头 信息
     *
     * 格式类似于
     *
     *      Content-Type: text/html; charset=UTF-8
     *
     * @param contentType
     * @throws IOException
     */
    private void sendResponseHeader(ContentType contentType) throws IOException {
        output.write(("Content-Type: "+contentType+"; charset=UTF-8\n\n").getBytes());
    }


    /**
     * 内部调用方法
     *
     * 往响应报文的 响应体 信息
     * 直接发送写好的文件
     *
     * @param file
     * @throws IOException
     */
    private void sendResponseBody(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[2048];
        int read;
        while ((read=bis.read(bytes))!=-1){
            output.write(bytes,0,read);
            output.flush();
        }
        bis.close();
    }

    /**
     * 内部调用方法
     *
     * 往响应报文的 响应体 信息
     * 发送字符串转换得到的 Byte[]
     *
     * @param bodyStr
     * @throws IOException
     */
    private void sendResponseBody(String bodyStr) throws IOException {
        output.write(bodyStr.getBytes());
    }


    /**
     * 外部可调用方法
     *
     * 返回封装好 JSON 数据的响应报文
     * 用户直接调用就可以向客户端返回 JSON 数据了
     *
     * @param json 需要发送的 JSON 文本串
     * @throws IOException
     */
    public void sendJsonData(String json) throws IOException {
        sendResponseLine(StateCode.CODE_200);
        sendResponseHeader(ContentType.APPLICTION_JSON);
        sendResponseBody(json);
    }

    /**
     * 外部可调用方法
     *
     * 返回封装好 文件数据 的响应报文
     * 用户直接调用就可以往客户端返回文件流了
     *
     * @param file 需要发送给客户端的文件
     *
     * @throws IOException
     */
    public void sendFileStream(File file) throws IOException {
        sendResponseLine(StateCode.CODE_200);
        sendResponseHeader(ContentType.APPLICTION_OCTET_STREAM);
        if (file.exists()){
            sLogUtil.debugLoger("请求文件： "+file.getAbsolutePath()+" 存在");
            sendResponseBody(file);
        }else {
            sendResponseBody(ErrorMsg.ERR_404_MSG);
        }
    }

    /**
     * 外部可调用方法
     *
     * 用户使用这个方法可关闭这个 Response 和 Request
     * 包括 Request 其中的 socket 连接
     * 并将全部资源设为 null
     *
     * @throws IOException
     */
    public void closeStream() throws IOException {
        this.requset.getBufferReader().close();
        this.requset.setBufferReader(null);
        this.getOutputStream().close();
        this.setOutput(null);
        this.requset.getSocket().close();
        this.requset = null;
        sLogUtil.debugLoger("关闭了 inputStream 和 outputStream 和请求的 Socket 连接");
    }
}
