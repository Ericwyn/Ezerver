import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.request.RequestParam;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 测试自定义 /test 路径处理 GET 和 POST 请求
 * 测试 Ajax 提交 POST 参数，包括
 *      正常的 POST 提交
 *      键值对提交
 *      JSON 数据提交
 *
 * HTML 代码在 /webroot/apiTest.html
 *
 * ---------------------------------------------
 *
 * Test GET and POST requests on /test
 * Test Ajax POST, include
 *      Normal Ajax POST
 *      key-value POST
 *      JSON data POST
 *
 * the HTML code in /webroot/apiTest.html
 *
 * Created by Ericwyn on 18-5-5.
 */
public class AjaxPostTest {
    public static void main(String[] args) throws Exception {

        HandleMethod testMethod = new HandleMethod("/test") {
            @Override
            public void RequestDo(Request request, Response response) throws IOException {

                HashMap<String, RequestParam> paramMap = request.getParamMap();
                System.out.println(request.getMethodName() + " 收到请求参数如下");
                for (String key:paramMap.keySet()){
                    System.out.println("key:"+key+"\tvalue:"+paramMap.get(key).getValue());
                }
                System.out.println();
                System.out.println("收到 JSON 参数如下");
                if (request.getJSONParamString()!=null){
                    System.out.println("json Data :" +request.getJSONParamString());
                }

                response.sendTextHtml("ok~");
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

 /// URI Encode！！！！

 4，发布！
 5，利用 Ezerver 重写 Leablog 后端

 */