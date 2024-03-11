package com.longsan;

import java.io.File;

public class RecursiveRemoveString {

    public static void main(String[] args) {
        // 指定目录路径
        String directoryPath = "/Users/longhao/Downloads";

        // 调用递归方法去除指定字符串
        recursiveRemoveString(directoryPath);
    }

    public static void recursiveRemoveString(String directoryPath) {
        // 创建File对象表示目录
        File directory = new File(directoryPath);

        // 检查目录是否存在
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("指定的路径不是一个有效的目录！");
            return;
        }

        // 获取目录下的所有文件和子目录
        File[] files = directory.listFiles();

        // 遍历所有文件和子目录
        for (File file : files) {
            // 如果是文件，则处理文件名
            if (file.isFile()) {
                // 获取原始文件名
                String originalFileName = file.getName();

                // 去除指定字符串
                String modifiedFileName = originalFileName.replace("【更多资源www.youxuan68.com】", "");

                // 创建新的文件对象
                File newFile = new File(directoryPath, modifiedFileName);

                // 重命名文件
                if (file.renameTo(newFile)) {
                    System.out.println("文件名修改成功：" + originalFileName + " -> " + modifiedFileName);
                } else {
                    System.err.println("文件名修改失败：" + originalFileName);
                }
            } else if (file.isDirectory()) {
                // 如果是子目录，则递归处理子目录
                recursiveRemoveString(file.getAbsolutePath());
            }
        }
    }
}
