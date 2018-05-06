import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;

/**
 *
 * 快速开始
 * 使用 Ezerver 作为静态服务器，以此访问项目根路径下 webroot 中的静态网页文件
 *
 *      访问地址是 localhost:9090/index.html，会看到 HTTP SERVER Welcome to Ezerver！和一张图片
 *
 * ----------------------------------------------------------------
 *
 * Quick Start
 * use Ezerver for static resource server.
 *
 *      visit localhost:9090/index.html, you can see the welcome page of Ezerver!
 *
 * Created by Ericwyn on 18-5-5.
 */
public class SimpleStart {
    public static void main(String[] args) throws WebServerException {
        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .allowDebug()
                .build();
        server.start();
    }
}
