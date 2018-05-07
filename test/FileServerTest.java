import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;

/**
 * Ezerver 允许被当成一个文件服务器来使用，让用户可以访问服务器上面的某个目录里面的文件
 * 类似于在 chrome 上面访问 ftp 服务器的效果
 * 只需要在新建 SimpleHttpServer 的时候，调用 Builder 的 allowShowDirRequest() 方法就可以了
 *
 * 你甚至可以自定义 服务器网页的头部样式，默认样式如下
 *       _____
 *      | ____|_______ _ ____   _____ _ __
 *      |  _| |_  / _ \ '__\ \ / / _ \ '__|
 *      | |___ / /  __/ |   \ V /  __/ |
 *      |_____/___\___|_|    \_/ \___|_|
 *      ----- A simple HTTP file server -----
 *      ----------------------------------------
 *
 * 如果你想要更换一个头部样式，只需要在调用 allowShowDirRequest() 往里面传入头部样式的 HTML 代码块就可以了
 *
 *
 * Created by Ericwyn on 18-5-6.
 */
public class FileServerTest {
    public static void main(String[] args) throws WebServerException {
        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .allowDebug()
                .allowShowDirRequest()
                .build();

        server.start();
    }
}
