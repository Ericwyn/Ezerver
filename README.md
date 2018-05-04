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

## TODO
 - 对 `GET` 和 `POST` 的请求参数进行解析
 - uri `/` 的默认映射或者转跳 
 - 404、302、等的各种 HTTP 返回状态码
 - 启动配置文件
 - ......
 


                