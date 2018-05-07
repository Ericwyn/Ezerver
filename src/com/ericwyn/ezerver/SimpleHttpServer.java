package com.ericwyn.ezerver;

import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParseUtil;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.ezerver.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Ezerver 主类
 *
 * Created by Ericwyn on 18-5-1.
 */
public class SimpleHttpServer {
    //默认的运行端口为 9090 端口
    private static int SERVER_PORT = 9090;

    //用户自定义端口
    private int newPort = -1;

    //默认的 WEBROOT 文件夹，在运行目录下的 webroot 文件夹
    public  static String WEB_ROOT = System.getProperty("user.dir")+ File.separator + "webroot";

    //用户自定义根目录
    private String newWebRoot;

    //服务的核心 ServerSocket，负责监听所有的 Socket 请求
    private ServerSocket serverSocket = null;

    //设定 ServerSocket 是否继续执行，为 false 时候，ServerSocket 将会 close，只能由 close() 方法设置
    private boolean continueAcceptFlag = true;

    //核心 ServerSocket 运行的线程
    private Thread awaitThread = null;

    //线程 List，存储当前 Ezerver 运行的所有线程
    private ArrayList<Thread> threadListForPrint;

    //flag 设定是否自动打印线程情况
    private boolean allowPrintThreadList = false;

    //是否有用户自定义的 handleMethod 的flag
    private boolean useHandleMethod = false;

    //是否在返回文件夹页面
    private boolean enableShowDirHtml = false;

    //自定义的各种处理方法，处理对特定 URI 的访问请求
    private HashMap<String,HandleMethod> handleMethodsMap = new HashMap<>();

    // 文件下载页面的 默认 Title
    private String fileDownloadPageTitle ;

    //日志工具，与 Ezerver 其他工具类共享
    public static LogUtil logUtil = new LogUtil();

    private SimpleHttpServer(){}

    /**
     *
     * SimpleHttpServer 只能使用该 Builder 创建
     */
    public static class Builder{
        private SimpleHttpServer server;
        public Builder(){
            server=new SimpleHttpServer();
        }

        /**
         * 设定 SimpleHttpServer 的运行端口
         *
         * @param serverPort
         * @return
         */
        public Builder setServerPort(int serverPort){
            this.server.setServerPort(serverPort);
            return this;
        }

        /**
         * 设定 WEBROOT 目录
         *
         * @param webRoot
         * @return
         */
        public Builder setWebRoot(String webRoot){
            this.server.setWebRoot(webRoot);
            return this;
        }

        /**
         * 是否使用 Debug 模式
         * 调用该方法的话将会开启 Debug 模式，通过 LogUtil 打印 Ezerver 运行情况
         * 打印内容包括各个请求报文的报文详情
         *
         * 默认的话 LogUtil 只会输出 Error 级别的信息
         *
         * @return
         */
        public Builder allowDebug(){
            this.server.debug();
            return this;
        }

        /**
         * 是否允许打印 Ezerver 的线程情况
         * 调用该方法后，SimpleHttpServer 在开启后会每隔一段时间就打印一遍 ThreadList 的情况
         *
         * @return
         */
        public Builder allowPrintThreadList(){
            this.server.allowPrintThreadList();
            return this;
        }

        /**
         * 是否允许打印 Ezerver 的线程情况
         * 调用该方法后，SimpleHttpServer 在开启后会每隔一段时间就打印一遍 ThreadList 的情况
         *
         * @return
         */
        public Builder allowShowDirRequest(){
            this.server.allowShowDirRequest();
            return this;
        }

        /**
         * 是否允许打印 Ezerver 的线程情况
         * 调用该方法后，SimpleHttpServer 在开启后会每隔一段时间就打印一遍 ThreadList 的情况
         *
         * @return
         */
        public Builder allowShowDirRequest(String downloadPageTitle){
            this.server.allowShowDirRequest();
            this.server.fileDownloadPageTitle = downloadPageTitle;
            return this;
        }

        /**
         * 添加对特定路径请求的自定义处理方法
         *
         * @param handleMethod
         * @return
         */
        public Builder addHandleMethod(HandleMethod handleMethod){
            this.server.addHandleMethod(handleMethod);
            return this;
        }

        public SimpleHttpServer build(){
            return this.server;
        }

    }

    private void setServerPort(int serverPort){
        newPort = serverPort;
    }

    private void setWebRoot(String webRoot){
        this.newWebRoot = webRoot;
    }

    /**
     * 开启 SimpleHttpServer 服务
     * 如果 WEBROOT 目录不存在的话，将会直接退出
     *
     * 开启 await 线程，作为 核心 ServerSocket 的运行线程，不断监听从 SERVER_PORT 端口发来的 socket 请求
     * 每收到一个 socket 请求，就将它分发给一个新的子线程处理
     *
     * 因为核心 ServerSocket 的 accept 方法会一直阻塞，所以 await 线程也会一直存在于后台
     * 除非在某次请求之后发现 continueAcceptFlag 变为了 false，那么 close ServerSocket
     * 并且会关闭 线程情况打印
     *
     * 该方法调用之后，也会判定是否需要开启 线程情况打印
     * 如果 线程情况打印 allowPrintThreadList 被设置为 true 的话
     *
     * 那么 核心 ServerSocket 线程，以及之后所有的对 Socket 处理的子线程都将会被加入到 ThreadList 里面
     *
     * @throws WebServerException
     */
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
            logUtil.debugLoger("新建 ServerSocket 运行在 "+SERVER_PORT+" 端口");
        }catch (Exception e){
            e.printStackTrace();
            throw new WebServerException("无法新建 ServerSocket ，有可能端口被占用，发生 IOE ：" + e.getMessage());
        }
        awaitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Socket socket = null;
                    try {
                        socket = serverSocket.accept();
                        if (continueAcceptFlag){
                            startSocketThread(socket);
                        }else {
                            if (allowPrintThreadList){
                                for (Thread thread:threadListForPrint){
                                    if (thread.isAlive()){
                                        thread.interrupt();
                                    }
                                }
                                threadListForPrint.clear();
                                threadListForPrint = null;
                            }
                            if (!serverSocket.isClosed()){
                                serverSocket.close();
                            }
                            break;
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                logUtil.usuallyPrintLogerln("awaitThread 监听到 Flag 以为 false 所以退出");
            }
        });
        awaitThread.setName("核心 ServerSocket 线程");
        if (allowPrintThreadList){
            logUtil.debugLoger("开启 ThreadList 状态打印");
            if (threadListForPrint == null){
                threadListForPrint = new ArrayList<>();
            }
            startPrintThreadLoger();
            threadListForPrint.add(awaitThread);
        }
        awaitThread.start();
        logUtil.debugLoger("核心 ServerSocket 线程 开启成功");
    }

    /**
     * 接受 ServerSocket 转发过来的 socket ，开启一个独立线程对 socket 的内容进行处理
     *
     * 每个请求都会有一个随机 0~10000 的整数标记
     * 新的子线程会先从 socket 里面解析出一个 Request 对象
     * 并通过 Request 来生成一个 Response对象
     *
     * 然后读取通过 Request 的 uri 以及 查看是否拥有自定义的特殊访问路径处理方法
     * 判断如何对 Request 和 Response 进行处理
     *
     * 如果是自定义的特殊访问路径，那么将会把 Request 和 Response 转发给自定义的 HandleMethod 处理
     * 否则会按照默认状态处理
     *
     * 默认状态处理就是将所有的请求看成是对静态资源的请求
     * 调用的就是 Response 的 sendStaticResource() 方法
     *
     *
     * @param socket
     */
    private void startSocketThread(Socket socket){
        int randomThreadNum = (int)(Math.random()*10000);
        logUtil.debugLoger("收到一个请求，编号"+randomThreadNum);
        Thread temp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //解析 socket 里面的 Requset 请求
                    Request request = RequestParseUtil.parseRequset(socket);
                    // 当 useHandleMethod 并且拥有对这个 uri 请求路径的处理方法的时候才处理
                    // 不然全部按照静态资源请求处理
                    if (request == null){
                        throw new WebServerException(randomThreadNum + "报文长度为0，报文舍弃");
                    }
                    if (useHandleMethod){
                        boolean canFindMatchHandleMethod = false;
                        //是否能够以 uriTemp 匹配上
                        String uriTemp = request.getUri().split("\\?")[0];
                        if (handleMethodsMap.get(uriTemp)!=null){
                            canFindMatchHandleMethod = true;
                        }
                        //是否能够以 uriTemp.replaceFirst("/","") 匹配上
                        if (!canFindMatchHandleMethod){
                            if (handleMethodsMap.get(uriTemp.replaceFirst("/",""))!=null){
                                uriTemp = uriTemp.replaceFirst("/","");
                                canFindMatchHandleMethod = true;
                            }
                        }
                        //如果能够匹配上的话,确保 强制匹配的优先级高于 正则匹配
                        if (canFindMatchHandleMethod){
                            HandleMethod handleMethod = handleMethodsMap.get(uriTemp);
                            Response response = new Response(request);
                            handleMethod.requestDo(request,response);
                            logUtil.debugLoger(randomThreadNum + "自定义 HandleMethod 处理完毕");
                            canFindMatchHandleMethod = true;
                        }else {
                            //如果不能匹配上了，再来找找正则匹配
                            for (String key:handleMethodsMap.keySet()){
                                if (handleMethodsMap.get(key).isMatchRegex()){
                                    //这样的话优先级看的是 handleMethod 在 map 里面的顺序啊有点迷幻啊
                                    if (request.getUri().split("\\?")[0].matches(key)){
                                        logUtil.debugLoger(randomThreadNum + "请求被转发到自定义的 HandleMethod");
                                        HandleMethod handleMethod = handleMethodsMap.get(key);
                                        Response response = new Response(request);
                                        handleMethod.requestDo(request,response);
                                        logUtil.debugLoger(randomThreadNum + "自定义 HandleMethod 处理完毕");
                                        canFindMatchHandleMethod = true;
                                        break;
                                    }
                                }
                            }
                        }
                        //如果都无法找到的话，那么就直接使用 静态返回
                        if (!canFindMatchHandleMethod){
                            Response response = new Response(request);
                            response.sendStaticResource();
                            logUtil.debugLoger(randomThreadNum + "请求返回处理完成");
                            response.closeStream();
                        }
                    } else {
                        Response response = new Response(request);
                        if (enableShowDirHtml){
                            // 如果允许展示下载页面的话，那么就传入 true 参数
                            // fileDownloadPageTitle 是文件下载页面的 title样式 的http代码
                            response.sendStaticResource(true,fileDownloadPageTitle);
                        }else {
                            response.sendStaticResource();
                        }
                        logUtil.debugLoger(randomThreadNum + "请求返回处理完成");
                        response.closeStream();
                    }
                } catch (IOException | WebServerException e) {
                    e.printStackTrace();
                } finally {
                    // input 和 output 以及 socket 的 close 都放到 Response 的 closeStream() 方法里面，在请求结束后全部关闭
                    try {
                        if (!socket.isClosed()){
                            socket.close();
                            logUtil.debugLoger(randomThreadNum + "请求的 socket 已经关闭");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        temp.setName("监听线程 "+ randomThreadNum);
        if (allowPrintThreadList){
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
            logUtil.debugLoger("将 continueAcceptFlag 设置为 false ");
            sendLastAcceptToServerSocket();
            logUtil.debugLoger("发送了最后一个请求");
            if (!serverSocket.isClosed()){
                serverSocket.close();
            }
            logUtil.debugLoger("关闭了 serverSocket ");
            logUtil.debugLoger("SimpleHttpServer 应该已经退出了");
            sendLastAcceptToServerSocket();
        }else {
            logUtil.debugLoger("线程未启动");
        }
    }

    /**
     * 发送最后一个请求结束核心 accept 方法的拥塞
     * 因为在将 continueAcceptFlag 设置为 false 之后 ServerSocket 仍有可能处在拥塞状态
     */
    private void sendLastAcceptToServerSocket() {
        //建立连接
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1",SERVER_PORT);
            OutputStream out = socket.getOutputStream() ;
            out.write("a".getBytes());
            out.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            logUtil.debugLoger("最终请求发送失败，ServerSocket 已经关闭");
        }

    }

    /**
     * 单独线程，每隔 5s 打印一遍 Ezerver 所有线程的情况，包括核心 ServerSocket 线程
     * 然后会将已经消亡的线程移出 ThreadList
     */
    private void startPrintThreadLoger(){
        ArrayList<Thread> removeList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!continueAcceptFlag){
                        logUtil.usuallyPrintLogerln("\t\t | continueAcceptFlag 为 false，线程结束");
                    }
                    if (threadListForPrint.size()==0){
                        logUtil.usuallyPrintLogerln("\t\t | 线程 List 已经清空，该 ThreadList 监听线程结束");
                        break;
                    }
                    logUtil.usuallyPrintLogerln("\t\t -------------------------------------");
                    logUtil.usuallyPrintLogerln("\t\t | 共有 " + threadListForPrint.size() + " 个线程");
                    for (int i = 0; i< threadListForPrint.size(); i++){
                        logUtil.usuallyPrintLoger("\t\t | No " + i+" : ");
                        if (threadListForPrint.get(i).isAlive()){
                            logUtil.usuallyPrintLogerln(threadListForPrint.get(i).getName()+" 线程建在");
                        }else {
                            logUtil.usuallyPrintLogerln(threadListForPrint.get(i).getName()+" 线程消亡");
                            removeList.add(threadListForPrint.get(i));
                        }
                    }
                    logUtil.usuallyPrintLogerln("\t\t | 清理 "+removeList.size()+" 个线程");
                    logUtil.usuallyPrintLogerln("\t\t -------------------------------------");
                    threadListForPrint.removeAll(removeList);
                    removeList.clear();
                }
            }
        }).start();
    }

    /**
     * 通过这个方法，为 ServerSocket 增加对 特定请求路径 的自定义处理
     *
     * @param handleMethod
     */
    private void addHandleMethod(HandleMethod handleMethod){
        if (!useHandleMethod){
            useHandleMethod = true;
        }
        handleMethodsMap.put(handleMethod.getUri().split("\\?")[0],handleMethod);
    }

    /**
     * 开启 Debug 状态 以开启 Debug 日志打印
     */
    private void debug(){
        SimpleHttpServer.logUtil.setPrintDebug(true);
    }

    /**
     * 开启 单独线程 打印 Ezerver 所有线程的情况
     */
    private void allowPrintThreadList(){
        allowPrintThreadList = true;
    }

    /**
     * 开启之后，如果一静态资源方式访问文件夹的话，那么将会返回文件页面
     * 一个静态资源服务器的文件夹页面
     *
     */
    private void allowShowDirRequest(){
        enableShowDirHtml = true;
    }
}
