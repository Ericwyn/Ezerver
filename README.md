# Ezerver
A simple web server implemented using java
 
## Quick Start
    
    public class SimpleStart {
        public static void main(String[] args) throws WebServerException {
            SimpleHttpServer server = new SimpleHttpServer.Builder()
                    .allowDebug()
                    .build();
            server.start();
        }
    }
    

然后访问 localhost:9090 端口就可以访问 webroot 里面的静态网页了

## More

If you need more examples please see the sample code in the `test` folder

Or read the Chinese documentation [Ezerver 中文文档](documents/Ezerver_doc_v1.md)