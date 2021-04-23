package com.shentu.gamebox.utils;

import android.util.Log;

import com.shentu.gamebox.BuildConfig;

public class LogUtils {
    /*类名*/
    static String className;
    /*方法名*/
    static String methodName;
    /*行数*/
    static int lineNumber;

    /*
     * 判断是否可以调试
     * */
    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        StringBuffer bf = new StringBuffer();
        bf.append("====================");
        bf.append(methodName);
        bf.append("(").append(className).append(lineNumber).append(")");
        bf.append(log);
        return bf.toString();
    }

    /*获取文件数
     * 方法名
     * 所在行数
     * */
    public static void getMethodName(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;
        getMethodName(new Throwable().getStackTrace());
        Log.e(className, createLog(message));

    }

    public static void i(String message) {
        if (!isDebuggable())
            return;
        getMethodName(new Throwable().getStackTrace());
        Log.i(className, createLog(message));

    }

    public static void d(String message) {
        if (!isDebuggable())
            return;
        getMethodName(new Throwable().getStackTrace());
        Log.d(className, createLog(message));

    }

    public static void v(String message) {
        if (!isDebuggable())
            return;
        getMethodName(new Throwable().getStackTrace());
        Log.v(className, createLog(message));

    }

    public static void w(String message) {
        if (!isDebuggable())
            return;
        getMethodName(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }
}
