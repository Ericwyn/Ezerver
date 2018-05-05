package com.ericwyn.ezerver.util;

/**
 * 日志打印工具
 *
 * Created by Ericwyn on 18-5-4.
 */
public class LogUtil {
    private boolean printDebug = false;
    private boolean printErrorLog = true;

    //printDebug 打印
    public void debugLoger(String msg){
        if (printDebug){
            System.out.println(msg);
        }
    }

    //printDebug 打印
    public void usuallyPrintLogerln(String msg){
        System.out.println(msg);
    }

    //error log 打印
    public void errorLoger(String msg){
        if (printErrorLog){
            System.out.println(msg);
        }
    }

    //无论如何都会打印出来
    public void usuallyPrintLoger(String msg){
        System.out.print(msg);
    }

    public void setPrintDebug(boolean printDebug) {
        this.printDebug = printDebug;
    }

    public void setPrintErrorLog(boolean printErrorLog) {
        this.printErrorLog = printErrorLog;
    }
}
