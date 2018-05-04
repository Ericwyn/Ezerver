package com.ericwyn.ezerver;

import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.ezerver.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * 简单 http server 的实现
 *
 * 参考 https://blog.csdn.net/martinwangjun/article/details/77659336
 *
 * 简单版本，单线程，只能处理1个请求，返回的只是 hello test server！ 页面而已
 *
 * http 协议返回
 *
 * Created by Ericwyn on 18-5-1.
 */
public class SimpleHttpServer {
    //默认 9090 端口
    private static int SERVER_PORT = 9090;
    private int newPort = -1;   //用户自定义端口
    public  static String WEB_ROOT = System.getProperty("user.dir")+ File.separator + "webroot";
    private String newWebRoot;  //用户自定义根目录
    private ServerSocket serverSocket = null;

    //这个 flag 设定是否 ServerSocket 继续执行
    private boolean continueAcceptFlag = true;

    private Thread awaitThread = null;

    private ArrayList<Thread> threadListForPrint;

    private boolean allowPrintThreadList = false;

    //    public void simpleServerTest() throws IOException, WebServerException {
//        ServerSocket ss = new ServerSocket(SERVER_PORT);
//        Socket socket = ss.accept();
//        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//        String buffer = null;
//        StringBuilder requsetLine = new StringBuilder();
//        while ((buffer = br.readLine())!=null && !buffer.equals("")){
//            requsetLine.append(buffer).append("\n");
//        }
//
//        Request request = Request.parseRequset(requsetLine.toString());
//
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        bw.write("HTTP/1.1 200 OK\n");
//        bw.write("Content-Type: text/html; charset=UTF-8\n\n");
//        bw.write("<html>\n" + "<head>\n" + "    <title>first page</title>\n"
//                + "</head>\n" + "<body>\n" + "    <h1>Hello Web Server!</h1>\n"
//                + "</body>\n" + "</html>\n");
//        bw.flush();
//        bw.close();
//
//        br.close();
//        socket.close();
//        ss.close();
//    }

    public SimpleHttpServer(){

    }

    public void setServerPort(int serverPort){
        newPort = serverPort;
    }

    public void setWebRoot(String webRoot){
        this.newWebRoot = webRoot;
    }

    public void start() throws WebServerException {
        if (newWebRoot!=null){
            WEB_ROOT = newWebRoot;
        }
        File rootDir = new File(WEB_ROOT);
        if (!rootDir.isDirectory()){
            throw new WebServerException("WEB ROOT 文件夹不存在，路径是 :"+rootDir.getAbsolutePath());
        }
        try {
            if (newPort!=-1){
                SERVER_PORT = newPort;
            }
            serverSocket = new ServerSocket(SERVER_PORT);
            LogUtils.debugLoger("新建 ServerSocket 运行在 "+SERVER_PORT+" 端口");
        }catch (Exception e){
            e.printStackTrace();
            throw new WebServerException("无法新建 ServerSocket ，有可能端口被占用，发生 IOE ：" + e.getMessage());
        }
        ServerSocket finalServerSocket = serverSocket;
        awaitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Socket socket = null;
                    try {
                        socket = finalServerSocket.accept();
                        if (continueAcceptFlag){
                            startSocketThread(socket);
                        }else {
                            break;
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LogUtils.usuallyPrintLogerln("awaitThread 监听到 Flag 以为 false 所以退出");
            }
        });
        awaitThread.setName("awaitThread serverSocket 监听");
        if (allowPrintThreadList){
            LogUtils.debugLoger("开启 ThreadList 状态打印");
            threadListForPrint.add(awaitThread);
            startPrintThreadLoger();
        }
        awaitThread.start();
        LogUtils.debugLoger("serverSocket 线程 开启成功");
    }

    private void startSocketThread(Socket socket){
        int randomThreadNum = (int)(Math.random()*10000);
        LogUtils.debugLoger("收到一个请求，编号"+randomThreadNum);
        Thread temp = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;
                OutputStream output = null;
                try {
                    //获取 socket 的 input 和 output
                    input = socket.getInputStream();
                    output = socket.getOutputStream();
                    //解析 socket 里面的 Requset 请求
                    Request request = Request.parseRequset(input);
                    //返回 Response 给 output
                    Response response = new Response(request,output);
                    response.sendStaticResource();
                    LogUtils.debugLoger(randomThreadNum + "请求返回处理完成");
                    socket.close();
                    LogUtils.debugLoger(randomThreadNum + "请求的 socket 已经关闭");

                } catch (IOException | WebServerException e) {
                    e.printStackTrace();
                } finally {
                    if (input!=null){
                        try {
                            input.close();
                            LogUtils.debugLoger(randomThreadNum + "请求的 socket input 已经关闭");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (output!=null){
                        try {
                            output.close();
                            LogUtils.debugLoger(randomThreadNum + "请求的 socket output 已经关闭");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        temp.setName("监听线程 "+ randomThreadNum);
        if (allowPrintThreadList){
            threadListForPrint = new ArrayList<>();
            threadListForPrint.add(temp);
        }
        temp.start();
    }

    /**
     * 关闭 awaith 线程
     *
     * 步骤如下
     *      1，先将 continueAcceptFlag 设置为 false
     *      2，向 serverSocket 发送一个请求，以结束拥塞状态
     *      3，关闭 serverSocket
     *
     * @throws Exception
     */
    public void close() throws Exception{
        if (awaitThread.isAlive()){
            continueAcceptFlag = false;
            LogUtils.debugLoger("将 continueAcceptFlag 设置为 false ");
            sendLastAcceptToServerSocket();
            LogUtils.debugLoger("发送了最后一个请求");
            closeServerSocket();
            LogUtils.debugLoger("关闭了 serverSocket ");
            LogUtils.debugLoger("SimpleHttpServer 应该已经退出了");
            sendLastAcceptToServerSocket();
        }else {
            LogUtils.debugLoger("线程未启动");
        }
    }

    private void closeServerSocket() throws IOException {
        if (!serverSocket.isClosed()){
            serverSocket.close();
        }
    }

    /**
     * 在将 continueAcceptFlag 设置为 false 之后要发送最有一个请求，接触 accept 的拥塞
     */
    private void sendLastAcceptToServerSocket() {
        //建立连接
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1",SERVER_PORT);
            OutputStream out = socket.getOutputStream() ;
            out.write("a".getBytes());
            out.close();
        } catch (IOException e) {
            LogUtils.debugLoger("最终请求发送失败，ServerSocket 已经关闭");
        }

    }

    //循环打印当前线程 List 情况
    private void startPrintThreadLoger(){
        ArrayList<Thread> removeList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (threadListForPrint.size()==0){
                        LogUtils.usuallyPrintLogerln("\t\t | 线程 List 已经清空，该 ThreadList 监听线程结束");
                        break;
                    }
                    LogUtils.usuallyPrintLogerln("\t\t -------------------------------------");
                    LogUtils.usuallyPrintLogerln("\t\t | 共有 " + threadListForPrint.size() + " 个线程");
                    for (int i = 0; i< threadListForPrint.size(); i++){
                        LogUtils.usuallyPrintLoger("\t\t | No " + i+" : ");
                        if (threadListForPrint.get(i).isAlive()){
                            LogUtils.usuallyPrintLogerln(threadListForPrint.get(i).getName()+" 线程建在");
                        }else {
                            LogUtils.usuallyPrintLogerln(threadListForPrint.get(i).getName()+" 线程消亡");
                            removeList.add(threadListForPrint.get(i));
                        }
                    }
                    LogUtils.usuallyPrintLogerln("\t\t | 清理 "+removeList.size()+" 个线程");
                    LogUtils.usuallyPrintLogerln("\t\t -------------------------------------");
                    threadListForPrint.removeAll(removeList);
                    removeList.clear();
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
