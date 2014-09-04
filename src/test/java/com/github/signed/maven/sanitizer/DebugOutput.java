package com.github.signed.maven.sanitizer;

public class DebugOutput {

    public void logCalledMethod(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[2];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();

        System.out.println(className+"."+methodName);
    }
}
