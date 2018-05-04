package test;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Ericwyn on 18-5-1.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        SimpleHttpServer server = new SimpleHttpServer();
//        server.setWebRoot("/media/ericwyn/Work/Chaos/meetwhy主页/meetwhy主页_2.0");
        server.setServerPort(8080);
        server.addHandleMethod(new HandleMethod("/test") {
            @Override
            public void RequestDo(Request request, OutputStream outputStream) throws IOException {
                HashMap<String, RequestParam> paramMap = request.getParamMap();
                System.out.println("request 的 请求参数共有 "+paramMap.size()+"个");
                outputStream.write("HTTP/1.1 200 OK\n".getBytes());
                outputStream.write("Content-Type: text/html; charset=UTF-8\n\n".getBytes());
                outputStream.write(("<html>\n" + "<head>\n" + "    <title>first page</title>\n"
                        + "</head>\n" + "<body>\n" + "    <h1>Hello Web Server!</h1>\n"
                        + "</body>\n" + "</html>\n").getBytes());
                outputStream.close();
            }
        });

        server.start();
        Scanner in = new Scanner(System.in);
        while (true){
            if (in.nextInt() == 404){
                System.out.println("收到关闭请求");
                server.close();
                break;
            }
        }
    }

}
