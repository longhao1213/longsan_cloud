package com.longsan.minio.utils;

import cn.hutool.core.lang.UUID;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author longhao
 * @since 2023/3/8
 */
public class FileUtils {

    /**
     * 切割文件
     *
     * @param srcFile    源文件路径
     * @param fileName   源文件名称
     * @param singleSize 每个分片大小
     * @return 切割后存放的文件夹
     */
    public static String cutFile(String srcFile, String fileName, int singleSize) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 创建新的文件夹
        File file = new File(srcFile.replace(fileName, "") + UUID.randomUUID().toString().replace("-", ""));
        file.mkdir();
        try (FileInputStream inputStream = new FileInputStream(srcFile);) {
            // 创建规定大小的byte数组
            byte[] b = new byte[singleSize];
            int len = 0;
            // index为切割出来的文件进行排序
            int index = 1;
            // 遍历把大文件读入byte数组当中，当byte数组读取满后写入对应小文件中
            while ((len = inputStream.read(b)) != -1) {
                FileOutputStream outputStream = new FileOutputStream(file.getPath() + "/" + index + "." + fileType);
                outputStream.write(b, 0, len);
                outputStream.close();
                index++;
            }
            return file.getPath();
        } catch (Exception e) {
            file.delete();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String s = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream("/Users/longhao/Downloads/5.1 并发编程之深入理解JMM&并发三大特性（一）【海量资源：666java.com】.mp4")));
        System.out.println(s);
    }
}
