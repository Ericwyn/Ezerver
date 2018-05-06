package com.ericwyn.ezerver.request;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * 这个工具类用来解析 socket
 * 解析 HTTP 报文，得到 Request 对象。
 *
 * Created by Ericwyn on 18-5-5.
 */
public class RequestParseUtil {
    private static LogUtil sLogUtil = SimpleHttpServer.logUtil;

    // 通过 socket 获取 inputStream，然后先读取 inputStream 的报文详情

    /**
     * 通过 socket 获取 inputStream，然后通过 inputStream 获取报文详情
     *
     * 使用 BufferReader 包装 InputStream 并用 UTF-8 来读取，避免了请求报文中的中文乱码问题
     *
     * 得到 报文之后，再将报文交给 parseRequsetGram() 方法解析
     *
     * @param socket socket 连接
     * @return
     * @throws IOException
     * @throws WebServerException
     */
    public static Request parseRequset(Socket socket) throws IOException, WebServerException {
        InputStream inputStream = socket.getInputStream();
        StringBuilder requestGramBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        char[] bytes = new char[inputStream.available()+100];
        int readLength;
        try {
            readLength = reader.read(bytes);
        }catch (Exception e){
            readLength =-1;
        }
        requestGramBuilder.append(bytes,0,readLength);

        sLogUtil.debugLoger("\n收到的报文如下，总长度为"+requestGramBuilder.length());
        sLogUtil.debugLoger("----------------------------");
        sLogUtil.debugLoger(new String(requestGramBuilder.toString().getBytes(),"UTF-8"));
        sLogUtil.debugLoger("----------------------------\n\n");

        return parseRequsetGram(socket,reader,requestGramBuilder.toString());
    }


    /**
     * 解析报文的内容，然后返回一个设置好参数的 Request
     * 会根据报文请求方法的不同而分别调用 GET 解析 或者 POST 解析
     *
     * @param socket socket 报文来自的 Socket 连接
     *               将 socket 存入 Request 以方便后续 Response 拿到 OutputStream 以及关闭连接
     * @param reader
     * @param requestGram    请求的报文详情
     *
     * @return  返回一个 解析得到的 Request 对象，包含
     *
     * @throws WebServerException
     */
    private static Request parseRequsetGram(Socket socket, BufferedReader reader, String requestGram) throws WebServerException{
        if (requestGram.length() == 0){
            return null;
        }
        //分割请求头和请求体，判断是 GET 请求还是 POST 请求，然后再分别解析具体的请求报文
        String[] requestLine = requestGram.split("\n");
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
                parsePOST(request,requestGram);
            }
        }
        return request;
    }

    /**
     * 对 GET 报文的解析
     *
     * @param request 目标 Request
     * @param requestLine 报文详情的 String[]
     */
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

    /**
     * 对 POST 报文的解析
     *
     * 由于 POST 请求还可能带有不同的 请求数据类型，所以解请求体的时候
     * 依据 ContentType 的不同，也分开了 3 种情况来判断
     *
     *      1，multipart/form-data
     *          这种情况会先进行分割，利用报文首部 ContentType 里面的 boundary 进行分割
     *          分割线的数量会比请求体当中参数的数量多 1
     *          然后再进一步切割并解析成 RequestParam
     *
     *      2，application/x-www-form-urlencoded
     *          这种情况会像解析GET请求的 URI 参数那样，其中主要使用 & 和 = 来分割
     *          直接将请求的参数解析成键值对成为 RequestParam
     *
     *      3，application/json
     *          这种情况的话将不会解析成 RequestParam
     *          而是直接存储到 Request 类里面的 JSONParamString 当中
     *
     * 这样的解析方法还是存在一些局限的，例如
     *      首先，使用 Ajax 提交 FormData
     *      然后，然后设置请求头 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
     * 那么会按照第 2 种方式解析第 1 种样式的参数，于是就无法得到正确的 RequestParm
     *
     * @param request 目标 Request
     * @param postGram post报文的详情
     *                 由于 POST 报文还带有请求体，所以需要先对整个 请求报文进行一些处理
     *                 不像 parseGET 方法那样，直接传入一个 String[]
     */
    private static void parsePOST(Request request,String postGram){
        String splitTemp = "%12hf81hf893%"; //替换掉 body 与header 的\r\n\r\n
        postGram = postGram.replaceFirst("\r\n\r\n",splitTemp);
        String[] requestGramTemp = postGram.split(splitTemp);

        String[] requestLine = requestGramTemp[0].split("\n");
        //解析请求头信息
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
                if (aParamListTemp.replaceAll("\n","").replaceAll("\r","").replaceAll("-","").trim().equals("")){
                    continue;
                }
                if (aParamListTemp.startsWith("\r\n")){
                    aParamListTemp = aParamListTemp.replaceFirst("\r\n","");
                }
                String[] temp2 = aParamListTemp.split("\r\n");
                String[] keyTemp = temp2[0].split(";");
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
                param.setValue(temp2[1]);
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
        // 目前还有bug， 的话
        // 那么将无法正确处理 RequestParam
    }
}
