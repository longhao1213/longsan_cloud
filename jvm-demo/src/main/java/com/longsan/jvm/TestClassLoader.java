package com.longsan.jvm;

public class TestClassLoader {
    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
        System.out.println(com.sun.management.OperatingSystemMXBean.class.getClassLoader());
        System.out.println(TestClassLoader.class.getClassLoader());

        System.out.println();

        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = appClassLoader.getParent();
        ClassLoader bootstrapLoader = extClassLoader.getParent();

        System.out.println("the bootstrapLoader : " + bootstrapLoader);
        System.out.println("the extClassloader : " + extClassLoader);
        System.out.println("the appClassLoader : " + appClassLoader);

        System.out.println();

//        System.out.println("bootstrapLoader加载以下文件:");
//
//        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
//        for (int i = 0; i < urls.length; i++) {
//            System.out.println(urls[i]);
//        }

        System.out.println();
        System.out.println("extClassloader加载以下文件:");
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println();
        System.out.println("appClassLoader加载以下文件:");
        System.out.println(System.getProperty("java.class.path"));
    }
}
