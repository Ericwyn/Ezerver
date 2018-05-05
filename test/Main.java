import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParam;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Ericwyn on 18-5-5.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HandleMethod testMethod = new HandleMethod("/test") {
            @Override
            public void RequestDo(Request request, Response response) throws IOException {
                // 如果要执行静态请求的话，只需要调用下面的方法就好了
                // 这样的话效果就和默认处理方式一样了

                HashMap<String, RequestParam> paramMap = request.getParamMap();
                System.out.println("请求的uri为"+request.getUri());
                System.out.println("param数量 " + paramMap.size());
                System.out.println("请求方法为" + request.getMethodName());
                System.out.println("json 参数为: "+paramMap.get(Request.JSON_PARAME_KEY).getValue());

                response.responseJsonData("{\"page\":88,\"name\":\"Hello\",\"code\":10}");

                response.closeStream();
            }
        };

        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .setServerPort(8080)
                .allowDebug()
                .addHandleMethod(testMethod)
                .build();

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


/*
TODO
 1, Build 模式 创建 SimpleHttpServer
 2，重新设计 Response
 3，完善对 POST 的支持，对 JSON 的支持
 4，发布！
 5，利用 Ezerver 重写 Leablog 后端

 */