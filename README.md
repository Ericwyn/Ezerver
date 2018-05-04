# Ezerver
一个简单的 java web 服务器

 
## 默认配置
 - 监听端口 9090
 - 静态网页 WEB_ROOT 位于 `webroot` 文件夹
 
## 使用示例


    public static void main(String[] args) throws Exception {
        
        // 新建一个 SimpleHttpServer
        SimpleHttpServer server = new SimpleHttpServer();
        // 设置 WebRoot 路径
        server.setWebRoot("/home/www/testWeb");
        // 设置监听端口号
        server.setServerPort(8080);
        // 开启服务
        server.start();
        Scanner in = new Scanner(System.in);
        while (true){
            if (in.nextInt() == -1){
                System.out.println("收到关闭请求");
                server.close();
                break;
            }
        }
        
    }
    

然后访问 localhost:8080 端口就可以了

## 对于请求的要求
### POST 请求
 - `POST` 提交的 `Content-Type` 只支持
    - `multipart/form-data`
    - `application/x-www-form-urlencoded`
 - 只支持键值对的提交

## 自定义对某个 uri 请求的处理
使用 `SimpleHttpServer.addHandleMethod(HandleMethod method)`方法可以自定义对某个访问路径的处理，例如自定义处理来自 `/test` 路径的访问
    
    SimpleHttpServer server = new SimpleHttpServer();
    
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

## TODO
 - ~~对 `GET` 和 `POST` 的请求参数进行解析~~ 完成
 - 引入对 json 的支持，以实现返回 json 数据，或者接受 json 请求
 - uri `/` 的默认映射或者转跳 
 - 404、302、等的各种 HTTP 返回状态码
 - 启动配置文件
 - ......
 