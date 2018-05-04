package com.ericwyn.ezerver.request;

import com.ericwyn.ezerver.expection.WebServerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
        return parseRequset(inputStreamString.toString());
    }

    public static Request parseRequset(String socketInputSteamString) throws WebServerException{
        String[] requsetLine = socketInputSteamString.split("\n");
        Request request=new Request();
        String[] requsetLine0 = requsetLine[0].split(" ");
        if (requsetLine0.length != 3){
            throw new WebServerException("Request 报文错误，无法解析首行请求行所以无法知道其请求方法");
        }else {
            if (requsetLine0[0].equals("GET")){
                request.method = METHOD_GET;
            }else if (requsetLine0[0].equals("POST")){
                request.method = METHOD_POST;
            }
            request.setUri(requsetLine0[1]);
            request.setVersion(requsetLine0[2]);
        }
        for (int i=1;i<requsetLine.length;i++){
            if (requsetLine[i].startsWith("Host: ")){
                request.setHost(requsetLine[i].replace("Host: ",""));
            }else if (requsetLine[i].startsWith("Connection: ")){
                request.setConnection(requsetLine[i].replace("Connection: ",""));
            }else if (requsetLine[i].startsWith("Pragma: ")){
                request.setPragma(requsetLine[i].replace("Pragma: ",""));
            }else if (requsetLine[i].startsWith("Cache-Control: ")){
                request.setCacheControl(requsetLine[i].replace("Cache-Control: ",""));
            }else if (requsetLine[i].startsWith("Upgrade-Insecure-Requests: ")){
                request.setUpgradeInsecureRequests(requsetLine[i].replace("Upgrade-Insecure-Requests: ",""));
            }else if (requsetLine[i].startsWith("User-Agent: ")){
                request.setUserAgent(requsetLine[i].replace("User-Agent: ",""));
            }else if (requsetLine[i].startsWith("Accept: ")){
                request.setAccept(requsetLine[i].replace("Accept: ",""));
            }else if (requsetLine[i].startsWith("Accept-Encoding: ")){
                request.setAcceptEncoding(requsetLine[i].replace("Accept-Encoding: ",""));
            }else if (requsetLine[i].startsWith("Accept-Language: ")){
                request.setAcceptLanguage(requsetLine[i].replace("Accept-Language: ",""));
            }else if (requsetLine[i].startsWith("Cookie: ")){
                request.setCookie(requsetLine[i].replace("Cookie: ",""));
            }
        }
        //解析参数
        if (request.method == METHOD_GET){
            // /?a=1&b=2
            if (request.getUri().contains("?")){
                String[] split = (request.getUri().split("\\?")[1]).split("&");
                for (String temp:split){
                    String[] temp2 = temp.split("=");
                    request.getParamMap().put(temp2[0],new RequestParam(temp2[0],temp2[1]));
                }
            }
        }else if (request.method == METHOD_POST){

        }
        return request;
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
