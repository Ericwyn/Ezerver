# Ezerver 
[![](https://jitpack.io/v/Ericwyn/Ezerver.svg)](https://jitpack.io/#Ericwyn/Ezerver)

A simple HTTP web server

## Get Ezerver

### Use Maven to import
 - Step 1. Add the JitPack repository to your build file
    
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>

 - Step 2. Add the dependency
 
        <dependency>
            <groupId>com.github.Ericwyn</groupId>
            <artifactId>Ezerver</artifactId>
            <version>-SNAPSHOT</version>
        </dependency>
 
### Get `.jar` lib
you can visit latest jitpack build log, it will tell you the path of jar file in latest building, like 


    Files: 
    com/github/Ericwyn/Ezerver/Alphe-2
    com/github/Ericwyn/Ezerver/Alphe-2/Ezerver-Alphe-2.jar
    com/github/Ericwyn/Ezerver/Alphe-2/Ezerver-Alphe-2.pom
    com/github/Ericwyn/Ezerver/Alphe-2/Ezerver-Alphe-2.pom.md5
    com/github/Ericwyn/Ezerver/Alphe-2/Ezerver-Alphe-2.pom.sha1
    com/github/Ericwyn/Ezerver/Alphe-2/build.log

and you can get the jar file with this path

    https://jitpack.io/com/github/Ericwyn/Ezerver/Alphe-2/Ezerver-Alphe-2.jar
 
## Quick Start
    
    public class SimpleStart {
        public static void main(String[] args) throws WebServerException {
            SimpleHttpServer server = new SimpleHttpServer.Builder()
                    .allowDebug()
                    .build();
            server.start();
        }
    }
    

visit `localhost:9090/index.html` and you can see the welcome page on `webroot` dir.

## More Example

 - If you need more examples please see the sample code in the `test` folder

 - or read the Chinese documentation [Ezerver 中文文档](documents/Ezerver_doc_v1.md)
 
## Example project

### Leablog
 - visit [Leablog-Github]()
 - A blog model for [Leanote](https://github.com/leanote/leanote)
 - Ezerver is used instead of Spring Boot as its backend framework.