package com.ericwyn.ezerver.request;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.util.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Ericwyn on 18-5-5.
 */
public class RequestParseUtil {
    private static LogUtils logUtils = SimpleHttpServer.logUtils;
    // 通过 socket 获取 inputStream，然后先读取 inputStream 的报文详情
    public static Request parseRequset(Socket socket) throws IOException, WebServerException {
        InputStream inputStream = socket.getInputStream();
        StringBuilder inputStreamString = new StringBuilder();
        //转成 UTF-8 避免乱码的问题
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line;
        char[] bytes = new char[inputStream.available()+100];
        int readLength;
        try {
            readLength = reader.read(bytes);
        }catch (Exception e){
            readLength =-1;
        }
        inputStreamString.append(bytes,0,readLength);

        logUtils.debugLoger("\n收到的报文如下，总长度为"+inputStreamString.length());
        logUtils.debugLoger("----------------------------");
        logUtils.debugLoger(new String(inputStreamString.toString().getBytes(),"UTF-8"));
        logUtils.debugLoger("----------------------------\n\n");

        return parseRequsetGram(socket,reader,inputStreamString.toString());
    }

    //解析报文的内容，然后返回一个设置好参数的 Request
    private static Request parseRequsetGram(Socket socket, BufferedReader reader, String socketInputSteamString) throws WebServerException{
        if (socketInputSteamString.length() == 0){
            return null;
        }
        //分割请求头和请求体，判断是 GET 请求还是 POST 请求，然后再分别解析具体的请求报文
        String[] requestLine = socketInputSteamString.split("\n");
        Request request=new Request(socket,reader);
        String[] requestLine0 = requestLine[0].split(" ");
        if (requestLine0.length != 3){
            throw new WebServerException("Request 报文错误，无法解析首行请求行所以无法知道其请求方法");
        }else {
            request.setUri(requestLine0[1]);
            request.setVersion(requestLine0[2]);
            if (requestLine0[0].equals("GET")){
                request.setMethod(Request.METHOD_GET);
                parseGET(request,requestLine);
            }else if (requestLine0[0].equals("POST")){
                request.setMethod(Request.METHOD_POST);
                parsePOST(request,socketInputSteamString);
            }
        }
        return request;
    }

    //解析 GET 报文
    private static void parseGET(Request request,String[] requestLine){
        for (int i=1;i<requestLine.length;i++){
            if (requestLine[i].startsWith("Host: ")){
                request.setHost(requestLine[i].replace("Host: ",""));
            }else if (requestLine[i].startsWith("Connection: ")){
                request.setConnection(requestLine[i].replace("Connection: ",""));
            }else if (requestLine[i].startsWith("Pragma: ")){
                request.setPragma(requestLine[i].replace("Pragma: ",""));
            }else if (requestLine[i].startsWith("Cache-Control: ")){
                request.setCacheControl(requestLine[i].replace("Cache-Control: ",""));
            }else if (requestLine[i].startsWith("Upgrade-Insecure-Requests: ")){
                request.setUpgradeInsecureRequests(requestLine[i].replace("Upgrade-Insecure-Requests: ",""));
            }else if (requestLine[i].startsWith("User-Agent: ")){
                request.setUserAgent(requestLine[i].replace("User-Agent: ",""));
            }else if (requestLine[i].startsWith("Accept: ")){
                request.setAccept(requestLine[i].replace("Accept: ",""));
            }else if (requestLine[i].startsWith("Accept-Encoding: ")){
                request.setAcceptEncoding(requestLine[i].replace("Accept-Encoding: ",""));
            }else if (requestLine[i].startsWith("Accept-Language: ")){
                request.setAcceptLanguage(requestLine[i].replace("Accept-Language: ",""));
            }else if (requestLine[i].startsWith("Cookie: ")){
                request.setCookie(requestLine[i].replace("Cookie: ",""));
            }
        }
        //解析参数
        if (request.getUri().contains("?")){
            String[] split = (request.getUri().split("\\?")[1]).split("&");
            for (String temp:split){
                String[] temp2 = temp.split("=");
                request.getParamMap().put(temp2[0],new RequestParam(temp2[0],temp2[1]));
            }
        }
    }

    //解析 POST 报文
    private static void parsePOST(Request request,String socketInputSteamString){
        String splitTemp = "%12hf81hf893%"; //替换掉 body 与header 的\r\n\r\n
        socketInputSteamString = socketInputSteamString.replaceFirst("\r\n\r\n",splitTemp);
        String[] requestGramTemp = socketInputSteamString.split(splitTemp);

        String[] requestLine = requestGramTemp[0].split("\n");
        for (int i=1;i<requestLine.length;i++){
            if (requestLine[i].startsWith("Host: ")){
                request.setHost(requestLine[i].replace("Host: ",""));
            }else if (requestLine[i].startsWith("Connection: ")){
                request.setConnection(requestLine[i].replace("Connection: ",""));
            }else if (requestLine[i].startsWith("Pragma: ")){
                request.setPragma(requestLine[i].replace("Pragma: ",""));
            }else if (requestLine[i].startsWith("Cache-Control: ")){
                request.setCacheControl(requestLine[i].replace("Cache-Control: ",""));
            }else if (requestLine[i].startsWith("Upgrade-Insecure-Requests: ")){
                request.setUpgradeInsecureRequests(requestLine[i].replace("Upgrade-Insecure-Requests: ",""));
            }else if (requestLine[i].startsWith("User-Agent: ")){
                request.setUserAgent(requestLine[i].replace("User-Agent: ",""));
            }else if (requestLine[i].startsWith("Accept: ")){
                request.setAccept(requestLine[i].replace("Accept: ",""));
            }else if (requestLine[i].startsWith("Accept-Encoding: ")){
                request.setAcceptEncoding(requestLine[i].replace("Accept-Encoding: ",""));
            }else if (requestLine[i].startsWith("Accept-Language: ")){
                request.setAcceptLanguage(requestLine[i].replace("Accept-Language: ",""));
            }else if (requestLine[i].startsWith("Cookie: ")){
                request.setCookie(requestLine[i].replace("Cookie: ",""));
            }else if (requestLine[i].startsWith("Content-Length: ")){
                request.setContentLength(Long.parseLong(requestLine[i].replace("Content-Length: ","").trim()));
            }else if (requestLine[i].startsWith("Origin: ")){
                request.setOrigin(requestLine[i].replace("Origin: ",""));
            }else if (requestLine[i].startsWith("Content-Type: ")){
                // 分割 Content-Type: multipart/form-data; boundary=----WebKitFormBoundarypMRuRv2ZaPFS7dUm
                String contentTypeTemp = requestLine[i].replace("Content-Type: ","");
                String[] split = contentTypeTemp.split(";");
                request.setContentType(split[0]);
                for (int k=1;k<split.length;k++){
                    if (split[k].contains("boundary=")){
                        request.setContentTypeBoundray(split[k].trim().replace("\r","").replace("\n","").split("=")[1]);
                    }
                }
            }else if (requestLine[i].startsWith("Referer: ")){
                request.setReferer(requestLine[i].replace("Referer: ",""));
            }
        }
        if (request.getContentType().contains("multipart/form-data")){
            String paramTemp = requestGramTemp[1].replaceAll("\r\n\r\n","\r\n");
            //需要去除末尾的两个 -- ，然后每个分割线前面也还有 --,
            //具体参考 http://blog.sina.com.cn/s/blog_3e3fcadd0100y61s.html
            String[] paramListTemp = paramTemp.substring(0,paramTemp.length()-2).split("--"+request.getContentTypeBoundray());
            for (String aParamListTemp : paramListTemp) {
                if (aParamListTemp.replaceAll("\n","").replaceAll("\r","").trim().equals("")){
                    continue;
                }
                String[] temp2 = aParamListTemp.split("\r\n");
                String[] keyTemp = temp2[1].split(";");
                RequestParam param = new RequestParam();
                for (String aKeyTemp : keyTemp) {
                    if (aKeyTemp.equals("")){
                        continue;
                    }
                    if (aKeyTemp.contains("Content-Disposition: ")) {
                        param.setContentDisposition(aKeyTemp.replace("Content-Disposition: ", "").trim());
                    }
                    if (aKeyTemp.contains("name=\"")) {
                        String key = aKeyTemp.replace("name=\"", "").trim();
                        param.setKey(key.substring(0, key.length() - 1));  //去掉前后的 双引号
                    }
                }
                param.setValue(temp2[2]);
                request.getParamMap().put(param.getKey(),param);
            }
        }else if (request.getContentType().contains("application/x-www-form-urlencoded")){
            String paramTemp = requestGramTemp[1].replaceAll("\r\n","");
            String[] split = paramTemp.split("&");
            for (String temp:split){
                String[] temp2 = temp.split("=");
                request.getParamMap().put(temp2[0],new RequestParam(temp2[0],temp2[1]));
            }
        }else if (request.getContentType().contains("application/json")){
            // json 数据文本
            request.setJSONParamString(requestGramTemp[1].replaceAll("\r\n",""));
        }
        // post 还能有这几种
        // text/xml 判断
        // 目前还有bug，如果 ajax 提交 formData ，然后设置请求头 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded"); 的话
        // 那么将无法正确处理 RequestParam
    }
}
