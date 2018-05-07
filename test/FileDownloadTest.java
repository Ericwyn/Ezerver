import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;

import java.io.File;
import java.io.IOException;

/**
 *
 * 你可以使用 Ezerver 做一个简单的文件服务器，配合 Ezerver 的自定义访问路径处理
 * 你能够动态的给用户返回文件供他们下载
 *
 * 例如，我们需要让用户使用account为 root ,password 为 pw 并使用 GET 来访问 /down 的时候，能够下载一张图片
 *      如果是使用了 POST 请求或者是参数不完整的话我们返回 404 错误
 *      如果是 account 或者 password 错误的话，我们就返回 access fail 提示
 *
 * server 运行在默认的 9090 端口
 * 用户通过 loaclhost:9090/down 下载文件
 *
 * --------------------------------------------------------------------------------------
 *
 * You can use Ezerver to make a simple file server with HandleMethod
 * and you can verify the user's identity
 *
 * For example,
 * suppose the user can only download files using a GET request
 * with the correct account parameter and the password parameter,
 * the account needs to be "root"
 * and the password needs to be "pw"
 *
 * If the verification is successful, the user can download the file
 * otherwise the user will get 404 pages
 *
 * notice,the download server running in 9090 port
 * the down path is loaclhost:9090/down
 *
 *
 * Created by Ericwyn on 18-5-6.
 */
public class FileDownloadTest {
    public static void main(String[] args) throws WebServerException {
        HandleMethod method = new HandleMethod("/down") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                if (request.getMethod() != Request.METHOD_GET
                        || request.getParamMap().size() != 2
                        || request.getParamMap().get("account") == null
                        || request.getParamMap().get("password") == null){
                    response.send404Page();
                }else {
                    String account;
                    String password;
                    if ((account = request.getParamMap().get("account").getValue())!= null
                            && account.equals("root")
                            && (password = request.getParamMap().get("password").getValue())!=null
                            && password.equals("pw")){
                        response.sendFileStream(new File("webroot/img/图.zip"));
                    }else {
                        response.sendTextHtml("access fail");
                    }
                }
                response.closeStream();
            }
        };

        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .addHandleMethod(method)
                .allowDebug()
                .build();
        server.start();
    }
}
