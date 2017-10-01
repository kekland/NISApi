package com.kekland.enis.Utilities;

import android.util.Log;

/**
 * Created by Gulnar on 29.09.2017.
 */

public class DebugLog {
    public final static boolean DEBUG = true;
    public static void i(String message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.i(className + "." + methodName + "():" + lineNumber, message);
            //TODO : Add Fabric
        }
    }
    public static void e(String message) {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.e(className + "." + methodName + "():" + lineNumber, message);
            //TODO : Add Fabric
        }
    }
}
