# Ezerver 中文文档

## 介绍

Ezerver 是一个基于 socket 的简易 web 服务器，有了它，你只需要几行 java 代码，就可以使用 java 搭建一个 web  服务器，以此来处理静态的 Http 请求，或者是编写一些 API 请求

该项目的Github地址是 [Github-Ezerver](https://github.com/Ericwyn/Giethoorn/tree/EzeServer)

## 快速开始
假设我们将一个静态网页`index.html`放在 `/home/www/testweb` 路径下
只需要使用以下代码，你就可以在 `localhost:8080/index.html` 访问到你的网页了


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

## 作为静态网站服务器使用
`Ezerver` 起初的设计就是为了做一个简易的静态网页服务器，这是`Ezerver`最基本的功能，只需要以下几个步骤就可以启动一个静态网页服务器，处理对静态资源的 `GET` 请求

 - 新建 SimpleHttpServer
    
        SimpleHttpServer server = new SimpleHttpServer();

 - 设定运行端口（默认运行在`9090`端口上）
 
        server.setServerPort(8080);
 
 - 设定`WEB ROOT`路径（默认使用工程路径下的 `webroot`文件夹所在路径）。注意，如果WEBROOT 路径不能正确的指向已经存在的文件夹的话，那么将会报错并退出。
 
        server.setWebRoot("/home/www/testWeb");
   
 - 启动服务
        
        server.start();
        
 - 关闭服务
        
        server.close();
       
       
## 处理 `POST` 请求
由于`Ezerver`尚在开发阶段，所以对于 `POST` 请求尚不能很好的支持，其中，对于 `POST` 请求有以下的要求：

 - `POST` 提交的 `Content-Type` 只支持
    - `multipart/form-data`
    - `application/x-www-form-urlencoded`
    - `application/json`
 - `multipart/form-data`或者`application/x-www-form-urlencoded` 只支持键值对形式提交
 - 所有请求的提交数据都将使用 `UTF-8` 解码

## 对特定请求路径的自定义处理
由于`Ezerver`还可以支持对特定访问路径的自定义处理，所以`Ezerver`除了作为一个静态网页服务器之外，还可以完成一些其他的功能，例如完成几个小小的API接口。

该功能主要依赖 `SimpleHttpServer.addHandleMethod(HandleMethod handleMethod)` 方法完成，通过重写匿名内部类里面的 `RequestDo(Request request, Response response)` 来实现功能

例如如果你想要实现对 `/test` 路径访问请求的自定义处理，那么只需要像下面这样就可以了

    server.addHandleMethod(new HandleMethod("/test") {
        @Override
        public void RequestDo(Request request, Response response) throws IOException {
            // 如果要执行静态请求的话，只需要调用下面的方法就好了
            // 这样的话效果就和默认处理方式一样了

            HashMap<String, RequestParam> paramMap = request.getParamMap();
            System.out.println("请求的uri为"+request.getUri());
            System.out.println("param数量 " + paramMap.size());
            System.out.println("请求方法为" + request.getMethodName());

            response.getOutputStream().write("HTTP/1.1 200 OK\n".getBytes());
            response.getOutputStream().write("Content-Type: text/html; charset=UTF-8\n\n".getBytes());
            response.getOutputStream().write(("<html>\n" + "<head>\n" + "    <title>first page</title>\n"
                    + "</head>\n" + "<body>\n" + "    <h1>Hello Web Server!</h1>\n"
                    + "</body>\n" + "</html>\n").getBytes());

            response.closeStream();
        }
    });

## 对 `JSON` 数据的支持
### 获取请求中的`JSON`数据
Request 类的方法能够解析一般的`GET`请求和`POST`请求的键值对参数，但是如果是`JSON`数据格式的`POST`请求的话，`Ezerver`默认将不会去解析参数数据，而是会把整段的`JSON`参数存储为一个 `RequestParam`，在 `Request`的 `ParamMap` 里面，`key`为 `Request.JSON_PARAME_KEY`。你可以通过`key`获取 `Request` 里面的`JSON`数据，并通过其他的库（如`Jackson` 或者 `fastjson` 来完成对 `JSON` 的解析），例如下面的方法，将接受并获取来自`/test` 的 `JSON` 请求数据

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

### 以`JSON`格式返回数据
`Ezerver` 也支持从服务器端以 `JSON` 格式返回数据，只需要在自定义的 HandleMethod 里面使用一行代码就可以了，由此你就可以使用`Ezerver`来开发一些简单的 API 了，使用示例如下，如果你希望通过 `/test` 返回 `JSON`数据的话，只要这样写就好了

    HandleMethod testMethod = new HandleMethod("/test") {
        @Override
        public void RequestDo(Request request, Response response) throws IOException {
        
            response.responseJsonData("{\"page\":88,\"name\":\"Hello\",\"code\":10}");
            
            response.closeStream();
        }
    };
    


## 调试帮助
`Ezerver`主要有下面两个方法帮助调试

### 开启 debug 日志打印
使用调试模式，控制台将打印详细的 debug 信息，包括各个请求报文的具体情况

    server.debug();

### 打印后台线程活动情况
    
    server.allowPrintThreadList();


