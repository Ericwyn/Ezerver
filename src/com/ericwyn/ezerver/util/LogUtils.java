package com.ericwyn.ezerver.util;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class LogUtils {
    private static boolean printDebug = true;
    private static boolean printErrorLog = true;

    //printDebug 打印
    public static void debugLoger(String msg){
        if (printDebug){
            System.out.println(msg);
        }
    }

    //printDebug 打印
    public static void usuallyPrintLogerln(String msg){
        System.out.println(msg);
    }

    //error log 打印
    public static void errorLoger(String msg){
        if (printErrorLog){
            System.out.println(msg);
        }
    }

    public static void usuallyPrintLoger(String msg){
        System.out.print(msg);
    }

}
