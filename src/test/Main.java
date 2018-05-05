package test;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParam;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;
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
            public void RequestDo(Request request, Response response) throws IOException {
                // 如果要执行静态请求的话，只需要调用下面的方法就好了
                // 这样的话效果就和默认处理方式一样了

                HashMap<String, RequestParam> paramMap = request.getParamMap();
                System.out.println("请求的uri为"+request.getUri());
                System.out.println("param数量 " + paramMap.size());
                System.out.println("请求方法为" + request.getMethodName());
                System.out.println("json 参数为: "+paramMap.get(Request.JSON_PARAME_KEY).getValue());

                response.getOutputStream().write("HTTP/1.1 200 OK\n".getBytes());
                response.getOutputStream().write("Content-Type: text/html; charset=UTF-8\n\n".getBytes());
                response.getOutputStream().write(("<html>\n" + "<head>\n" + "    <title>first page</title>\n"
                        + "</head>\n" + "<body>\n" + "    <h1>Hello Web Server!</h1>\n"
                        + "</body>\n" + "</html>\n").getBytes());

                response.closeStream();
            }
        });

        server.debug();

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
