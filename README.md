# Ezerver
基于 Socket 实现的一个简易的的 Web 服务器

 
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

更多的文档说明请查看 [Ezerver 中文文档](documents/Ezerver_doc_v1.md)

## TODO
 - ~~对 `GET` 和 `POST` 的请求参数进行解析~~ 完成
 - 引入对 json 的支持，以实现返回 json 数据，或者接受 json 请求
 - uri `/` 的默认映射或者转跳 
 - 404、302、等的各种 HTTP 返回状态码
 - 启动配置文件
 - ......
 