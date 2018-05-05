package com.ericwyn.ezerver.request;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.util.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
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


    // 通用 报文 Header
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

    // POST 报文特殊 Header
    private long ContentLength;   //content 长度
    private String Origin;          //来源
    private String contentType;
    private String ContentTypeBoundray; //post 数据 分割线
    private String referer;

    //socket 的 inputStream 的包装
    private BufferedReader bufferReader;

    //存储参数
    private HashMap<String,RequestParam> paramMap;

    //存储 json 数据
    private String JSONParamString = null;

    private Socket socket;

    //设置该 Request 来自的 Socket 和读取的 bufferReader
    // bufferReader 为了解决请求报文中文乱码的问题
    public Request(Socket socket, BufferedReader bufferReader){
        this.socket = socket;
        this.bufferReader = bufferReader;
    }

    public String getJSONParamString(){
        return this.JSONParamString;
    }

    public void setJSONParamString(String JSONParamString) {
        this.JSONParamString = JSONParamString;
    }

    public int getMethod() {
        return method;
    }

    protected void setMethod(int method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return connection;
    }

    protected void setConnection(String connection) {
        this.connection = connection;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    protected void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    protected void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccept() {
        return accept;
    }

    protected void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    protected void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    protected void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getCookie() {
        return cookie;
    }

    protected void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getPragma() {
        return Pragma;
    }

    protected void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getUpgradeInsecureRequests() {
        return UpgradeInsecureRequests;
    }

    protected void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        UpgradeInsecureRequests = upgradeInsecureRequests;
    }

    public long getContentLength() {
        return ContentLength;
    }

    protected void setContentLength(long contentLength) {
        ContentLength = contentLength;
    }

    public String getOrigin() {
        return Origin;
    }

    protected void setOrigin(String origin) {
        Origin = origin;
    }

    public String getContentType() {
        return contentType;
    }

    protected void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReferer() {
        return referer;
    }

    protected void setReferer(String referer) {
        this.referer = referer;
    }

    public String getContentTypeBoundray() {
        return ContentTypeBoundray;
    }

    public void setContentTypeBoundray(String contentTypeBoundray) {
        ContentTypeBoundray = contentTypeBoundray;
    }

    public BufferedReader getBufferReader() {
        return bufferReader;
    }

    public void setBufferReader(BufferedReader bufferReader) {
        this.bufferReader = bufferReader;
    }

    public Socket getSocket() {
        return socket;
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
