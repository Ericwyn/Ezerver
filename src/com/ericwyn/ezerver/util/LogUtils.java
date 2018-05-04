package com.ericwyn.ezerver.util;

/**
 * Created by Ericwyn on 18-5-4.
 */
public class LogUtils {
    private static boolean debug = true;

    public static void debugLoger(String msg){
        if (debug){
            System.out.println(msg);
        }
    }

    public static void usuallyPrintLogerln(String msg){
        System.out.println(msg);
    }

    public static void usuallyPrintLoger(String msg){
        System.out.print(msg);
    }

}
