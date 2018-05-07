package com.ericwyn.ezerver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Ericwyn on 18-5-7.
 */
public class HtmlUtils {


    /**
     * 构造文件服务器 Html 网页的方法
     * 使用该方法构造的 Html 代码，将使用默认的 展示 Title
     * 默认 title 如下
     *
     *      _____
     *     | ____|_______ _ ____   _____ _ __
     *     |  _| |_  / _ \ '__\ \ / / _ \ '__|
     *     | |___ / /  __/ |   \ V /  __/ |
     *     |_____/___\___|_|    \_/ \___|_|
     *
     *     ----- A simple HTTP file server -----
     *
     *     ----------------------------------------
     *
     *
     * @param dir 文件夹
     *
     * @return
     */
    public static String makeDirHtml(File dir){
        return makeDirHtml(dir,defaultTitle);
    }

    /**
     * 构造文件服务器 Html 网页的方法
     *
     *  <tr>
     *      <td>
     *          &nbsp;&nbsp;
     *          <a href="http://192.168.199.154:9999" download="欢迎文件.txt.txt">欢迎文件.txt</a>
     *          &nbsp;&nbsp;&nbsp;&nbsp;
     *      </td>
     *      <td>
     *          &nbsp; 1 kB &nbsp;
     *      </td>
     *  </tr>
     *
     * @param dir 文件夹
     * @param defaultTitle title 的 HTML 代码
     *
     * @return
     */
    public static String makeDirHtml(File dir,String defaultTitle){
        if (defaultTitle!=null){
            setDefaultTitle(defaultTitle);
        }
        StringBuffer result=new StringBuffer();
        result.append(getHttpStringHead());
        if (dir==null){
            return result.append(getHttpStringTail()).toString();
        }
        ArrayList<File> dirList = new ArrayList<>();
        ArrayList<File> fileList = new ArrayList<>();
        for (File file:dir.listFiles()){
            if (file.isDirectory()){
                dirList.add(file);
            }else if (file.isFile()){
                fileList.add(file);
            }
        }
        dirList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        fileList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File file:dirList){
            result.append("<tr>\n");
            result.append("<td>\n");
            result.append("&nbsp;&nbsp;");
            result.append("<a href=\"\" class=\"next\">"+file.getName()+"</a>");
            result.append("&nbsp;&nbsp;&nbsp;&nbsp;\n");
            result.append("</td>\n");
            result.append("<td>\n");
            result.append("&nbsp; "+FileUtils.getAutoFileOrFilesSize(file.getAbsolutePath())+" &nbsp;\n");
            result.append("</td>");
            result.append("</tr>");
        }

        for (File file:fileList){
            result.append("<tr>\n");
            result.append("<td>\n");
            result.append("&nbsp;&nbsp;");
            result.append("<a href=\"\" class=\"next\">"+file.getName()+"</a>");
            result.append("&nbsp;&nbsp;&nbsp;&nbsp;\n");
            result.append("</td>\n");
            result.append("<td>\n");
            result.append("&nbsp; "+FileUtils.getAutoFileOrFilesSize(file.getAbsolutePath())+" &nbsp;\n");
            result.append("</td>");
            result.append("</tr>");
        }

        result.append(getHttpStringTail());
        return result.toString();
    }

    private static void setDefaultTitle(String titleHtml){
        defaultTitle = titleHtml;
    }

    private static String defaultTitle =
            "<pre>\n" +
            "   _____\n" +
            "  | ____|_______ _ ____   _____ _ __\n" +
            "  |  _| |_  / _ \\ '__\\ \\ / / _ \\ '__|\n" +
            "  | |___ / /  __/ |   \\ V /  __/ |\n" +
            "  |_____/___\\___|_|    \\_/ \\___|_|\n" +
            "\n" +
            " ----- A simple HTTP file server -----\n" +
            "\n" +
            "----------------------------------------\n" +
            "\n" +
            "</pre>" ;

    private static String getHttpStringHead(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title id=\"title\"> Ezerver </title>\n" +
                "    <style type=\"text/css\">\n" +
                "      h1{\n" +
                "        font-size:46px\n" +
                "      }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                defaultTitle+
                "<table style=\"font-size:24px\">\n" +
                "    <thead>\n" +
                "    <tr class=\"header\" id=\"theader\">\n" +
                "        <th>名称</th>\n" +
                "        <th>大小</th></tr>\n" +
                "    </thead>\n" +
                "<tbody id = \"list\">\n" +
                "    <tr id=\"goback\" style=\"display: none\">\n" +
                "        <td>\n" +
                "            &nbsp;&nbsp;<a href=\"\" id=\"back\">返回上层目录</a>&nbsp;&nbsp;&nbsp;&nbsp;\n" +
                "        </td>\n" +
                "    </tr>";
    }

    private static String  getHttpStringTail(){
        return "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "<script>\n" +
                "    var list = document.getElementsByClassName(\"next\");\n" +
                "    for (var i=0;i<list.length;i++){\n" +
                "        if(window.location.pathname !== \"/\"){\n" +
                "            list[i].href = (window.location.pathname+\"/\"+list[i].text).replace(\"//\",\"/\");\n" +
                "        }else {\n" +
                "            list[i].href = list[i].text;\n" +
                "        }\n" +
                "    }\n" +
                "    if(window.location.pathname !== \"/\"){\n" +
                "        var href = window.location.href;\n" +
                "        if (href.endsWith(\"/\")){\n" +
                "            href = href.substr(0,href.length-1);\n" +
                "        }\n" +
                "        var temp = href.split(\"/\");\n" +
                "        document.getElementById(\"back\").href = href.substr(0,href.length-temp[temp.length-1].length);\n" +
                "        document.getElementById(\"goback\").style.display = \"inline\";\n" +
                "    }\n" +
                "</script>" +
                "</html>";
    }

}
