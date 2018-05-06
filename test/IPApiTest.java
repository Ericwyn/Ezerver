import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 这个 Test 展示如何使用 Ezerver 完成一些简单的API
 *
 * 比如我们要编写一个API，如果用户往这里发送一个GET 请求的话，那么我们就会返回用户的 IP 地址
 * 返回的 JSON 数据的格式如下
 *
 *      {
 *          "ip": "10.50.17.1"
 *      }
 *
 * -------------------------------------------------------
 *
 * this Test show how to make a API server
 * we will make a API, when user send a GET request to this path
 * we will return a JSON data of the user's IP address
 * the JSON data like this
 *
 *      {
 *          "ip": "10.50.17.1"
 *      }
 *
 * Created by Ericwyn on 18-5-6.
 */
public class IPApiTest {
    public static void main(String[] args) throws WebServerException {

        HandleMethod method = new HandleMethod("/ip") {
            @Override
            public void RequestDo(Request request, Response response) throws IOException {
                String resJsonDataBuilder = null;
                if (request.getXForwardedFor()!=null){
                    // 直接通过 X-Forwarded-For 来获取 ip 参数
                    resJsonDataBuilder = "{\"ip\": \"" +
                            request.getXForwardedFor().replaceAll("\r","").replaceAll("\n","").replaceAll(" ","") +
                            "\"}";
                }else {
                    //通过 socket 获取 remoteSocketAddress
                    SocketAddress remoteSocketAddress = request.getSocket().getRemoteSocketAddress();
                    resJsonDataBuilder = "{\"ip\": \"" +
                            ((InetSocketAddress) remoteSocketAddress).getHostString() +
                            "\"}";
                }
                response.sendJsonData(resJsonDataBuilder);
                response.closeStream();
            }
        };

        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .setServerPort(8661)
                .addHandleMethod(method)
                .allowDebug()
                .build();
        server.start();
    }
}
