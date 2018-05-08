# Ezerver 中文文档

## 介绍

Ezerver 是一个基于 socket 的简易 web 服务器，有了它，你只需要几行 java 代码，就可以使用 java 搭建一个 web  服务器，以此来处理静态的 Http 请求，或者是编写一些 API 请求

# 快速开始
使用 Ezerver 作为静态服务器，以此访问项目根路径下 webroot 中的静态网页文件

运行下面的代码,访问地址是 localhost:9090/index.html，会看到 Ezerver 的欢迎页面了

    SimpleHttpServer server = new SimpleHttpServer.Builder()
                    .allowDebug()
                    .build();
            server.start();

完整的示例在 `test/SimpleStart.java` 里面

------

# Request 与 Response 说明
## Request

## Response

------

# 作为静态服务器使用
## 作为静态网页服务器
使用默认配置, Ezerver 就能开启一个静态文件服务器了,如果你想要设置静态网页服务器的 WEBROOT 的话, 请使用 `Builder.setWebRoot(String webRoot) `方法, 如下

    SimpleHttpServer server = new SimpleHttpServer.Builder()
                    .setWebRoot("/home/www")
                    .allowDebug()
                    .build();
     server.start();

## 作为静态文件服务器
### 允许请求文件夹路径
使用 `Builder.allowShowDirRequest() `方法, Ezerver 将允许用户访问文件夹路径, 当用户的访问 URI 指向一个文件夹时候,例如 `/img` , 那么将会显示一个网页列出当前目录下全部的文件与文件夹, 如下图

![fileserver](img/fileserver.png)

Server 开启的代码如下
    
        SimpleHttpServer server = new SimpleHttpServer.Builder()
                        .allowShowDirRequest()
                        .allowDebug()
                        .build();
         server.start();

完整的示例代码在 `/test/FileServerTest.java`

### 自定义网页头部样式
往 `allowShowDirRequest()` 方法当中传入 HTML 代码字符串,可以替换掉默认文件展示页面的头部样式

------

# 编写动态服务
 除了作为开启静态服务器的工具之外, Ezerver 允许用户针对特定 uri 请求进行特定的而返回, 由此完成一些 API 编写或者其他的事务处理
 
## 自定义特殊访问路径处理方法
Ezerver 对于特定访问 uri 的处理方法全部使用 `HandleMethod` 类来开启, 用户只需要新建一个 `HandleMethod` 类的实例, 实现里面的 `requestDo(Request request,Response response)` 方法, 并使用 `Builder.addHandleMethod(HandleMethod method)`将其加入到 Server 里面, 就可以完成对特定 uri 的访问处理.

如下面代码所示, 我们编写一个 `HandleMethod` 完成对来自 `/test` 的请求的处理, 让请求都返回 404 页面.

    HandleMethod method = new HandleMethod("/test") {
        @Override
        public void requestDo(Request request, Response response) throws IOException {
            response.send404Page();
            response.closeStream();
        }
    };

**注意!!!**, 所有的 `requestDo` 方法, 都应在最后调用 `response.closeStream()`

### 使用正则表达式匹配特殊 uri 
### 使用字符串匹配 uri

## 接受请求参数
### 获取请求参数
### 获取 JSON 请求数据

## 直接返回 HTML 文本/文件

## 直接返回 JSON 数据

## 直接返回文件

## 直接返回 404 错误页面

## 关闭当前请求连接

# 服务器开启与关闭
## Builder
## 关闭服务器

# 日志打印


------

# 使用示例


