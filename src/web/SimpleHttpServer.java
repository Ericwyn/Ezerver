package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * 简单 http server 的实现
 *
 * 参考 https://blog.csdn.net/martinwangjun/article/details/77659336
 *
 * 简单版本，单线程，只能处理1个请求，返回的只是 hello web server！ 页面而已
 *
 * http 协议返回
 *
 * Created by Ericwyn on 18-5-1.
 */
public class SimpleHttpServer {
    private static final int SERVER_PORT = 9090;
    public static final String WEB_ROOT = System.getProperty("user.dir")+ File.separator + "webroot";
    private static final String SHUTDOWN_COMMAND = "/QUIT";

    public void simpleServerTest() throws IOException, WebServerException {
        ServerSocket ss = new ServerSocket(SERVER_PORT);
        Socket socket = ss.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String buffer = null;
        StringBuilder requsetLine = new StringBuilder();
        while ((buffer = br.readLine())!=null && !buffer.equals("")){
            requsetLine.append(buffer).append("\n");
        }

        Request request = Request.parseRequset(requsetLine.toString());

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("HTTP/1.1 200 OK\n");
        bw.write("Content-Type: text/html; charset=UTF-8\n\n");
        bw.write("<html>\n" + "<head>\n" + "    <title>first page</title>\n"
                + "</head>\n" + "<body>\n" + "    <h1>Hello Web Server!</h1>\n"
                + "</body>\n" + "</html>\n");
        bw.flush();
        bw.close();

        br.close();
        socket.close();
        ss.close();
    }

    public void await() throws WebServerException{
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        }catch (Exception e){
            e.printStackTrace();
            throw new WebServerException("无法新建 ServerSocket ，有可能端口被占用，发生 IOE ：" + e.getMessage());
        }
        while (true){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                Request request = Request.parseRequset(input);
                System.out.println(request.getMethod());
            }catch (IOException e) {
                e.printStackTrace();
                throw new WebServerException("发生错误");
            }
        }

    }

}
