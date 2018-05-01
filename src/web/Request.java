package web;

import java.io.IOException;
import java.io.InputStream;

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
    private String uril;
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
//        while ((inputStream.read(bytes))!=-1){
//            for(int i = 0; i < bytes.length; i++) {
//                inputStreamString.append((char)bytes[i]);
//            }
//        }
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
            request.setUril(requsetLine0[1]);
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
        return request;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUril() {
        return uril;
    }

    public void setUril(String uril) {
        this.uril = uril;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getPragma() {
        return Pragma;
    }

    public void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getUpgradeInsecureRequests() {
        return UpgradeInsecureRequests;
    }

    public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        UpgradeInsecureRequests = upgradeInsecureRequests;
    }
}
