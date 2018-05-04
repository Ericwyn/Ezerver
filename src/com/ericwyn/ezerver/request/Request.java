package com.ericwyn.ezerver.request;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import sun.security.util.Length;

/**
 *
 * 请求的封装类
 *
 * Created by Ericwyn on 18-5-1.
 */
public class Request {
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;

    private final static int BUFFER_SIZE = 1024;
    private static LogUtils logUtils = SimpleHttpServer.logUtils;

    private int method;
    private String uri;
    private String version;
    private String host;
    private String Pragma;
    private String connection;
    private String cacheControl;
    private String userAgent;
    private String accept;
    private String acceptEncoding;
    private String acceptLanguage;
    private String cookie;
    private String UpgradeInsecureRequests;

    // POST 报文还带有
    private long ContentLength;   //content 长度
    private String Origin;          //来源
    private String contentType;
    private String ContentTypeBoundray; //post 数据 分割线
    private String referer;


    private HashMap<String,RequestParam> paramMap;


    private Request(){

    }

    public static Request parseRequset(InputStream inputStream) throws IOException, WebServerException {
        byte[] bytes = new byte[BUFFER_SIZE];
        StringBuilder inputStreamString = new StringBuilder();
        int readLength;
        try {
            readLength = inputStream.read(bytes);
        }catch (Exception e){
            readLength =-1;
        }
        for(int i = 0; i < readLength; i++) {
            inputStreamString.append((char)bytes[i]);
        }
        logUtils.debugLoger("\n收到的报文如下，总长度为"+inputStreamString.length());
        logUtils.debugLoger("----------------------------");
        logUtils.debugLoger(inputStreamString.toString());
        logUtils.debugLoger("----------------------------\n\n");
        return parseRequset(inputStreamString.toString());
    }

    public static Request parseRequset(String socketInputSteamString) throws WebServerException{
        if (socketInputSteamString.length() == 0){
            return null;
        }
        String[] requestLine = socketInputSteamString.split("\n");
        Request request=new Request();
        String[] requestLine0 = requestLine[0].split(" ");
        if (requestLine0.length != 3){
            throw new WebServerException("Request 报文错误，无法解析首行请求行所以无法知道其请求方法");
        }else {
            request.setUri(requestLine0[1]);
            request.setVersion(requestLine0[2]);
            if (requestLine0[0].equals("GET")){
                request.method = METHOD_GET;
                parseGET(request,requestLine);
            }else if (requestLine0[0].equals("POST")){
                request.method = METHOD_POST;
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
        }
        // post 还能有这几种
        // application/json 判断
        // text/xml 判断
        // 目前还有bug，如果 ajax 提交 formData ，然后设置请求头 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded"); 的话
        // 那么将无法正确处理 RequestParam
    }

    public int getMethod() {
        return method;
    }

    private void setMethod(int method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    private void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    private void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return connection;
    }

    private void setConnection(String connection) {
        this.connection = connection;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    private void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccept() {
        return accept;
    }

    private void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    private void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    private void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getCookie() {
        return cookie;
    }

    private void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getPragma() {
        return Pragma;
    }

    private void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getUpgradeInsecureRequests() {
        return UpgradeInsecureRequests;
    }

    private void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        UpgradeInsecureRequests = upgradeInsecureRequests;
    }

    public long getContentLength() {
        return ContentLength;
    }

    private void setContentLength(long contentLength) {
        ContentLength = contentLength;
    }

    public String getOrigin() {
        return Origin;
    }

    private void setOrigin(String origin) {
        Origin = origin;
    }

    public String getContentType() {
        return contentType;
    }

    private void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReferer() {
        return referer;
    }

    private void setReferer(String referer) {
        this.referer = referer;
    }

    public String getContentTypeBoundray() {
        return ContentTypeBoundray;
    }

    public void setContentTypeBoundray(String contentTypeBoundray) {
        ContentTypeBoundray = contentTypeBoundray;
    }

    public HashMap<String, RequestParam> getParamMap() {
        if (paramMap==null){
            paramMap = new HashMap<>();
        }
        return paramMap;
    }


    public String getMethodName(){
        if (this.method == METHOD_GET){
            return "GET";
        }
        if (this.method == METHOD_POST){
            return "POST";
        }
        return "未知方法";
    }

}
