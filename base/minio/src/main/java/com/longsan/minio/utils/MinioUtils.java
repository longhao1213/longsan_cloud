package com.longsan.minio.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.URLDecoder;
import com.longsan.minio.config.MinioConfig;
import com.longsan.minio.config.ThreadPoolTaskConfig;
import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetBucketPolicyArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.longsan.minio.config.MinioConfig.BUCKET_PARAM;

/**
 * @author longhao
 * @since 2023/3/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;


    /**************** bucket相关 *************/

    /**
     * 创建bucket
     *
     * @param bucketName
     */
    @SneakyThrows
    private boolean createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return false;
        }
        return true;
    }


    /**
     * 修改bucket策略
     * @param bucketName
     */
    @SneakyThrows
    public void setBucket(String bucketName,String policy) {
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy.replace(BUCKET_PARAM, bucketName))
                .build());
    }

    /**
     * 判断bucket是否存在
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    private boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 获取bucket策略
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public String getBucketPolicy(String bucketName) {
        return minioClient.getBucketPolicy(GetBucketPolicyArgs
                .builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 获取所有bucket列表
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }


    /**
     * 获取bucket相关信息
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return getAllBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 删除bucket
     *
     * @param bucketName
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**************** file相关 *************/


    /**
     * 判断文件是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public boolean isObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件是否存在, 异常：", e);
            exist = false;
        }
        return exist;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public boolean isFolderExist(String bucketName, String objectName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && objectName.equals(objectName)) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            log.error("[Minio工具类]>>>> 判断文件夹是否存在，异常：", e);
            exist = false;
        }
        return exist;
    }

    /**
     * 根据文件前缀查询文件
     *
     * @param bucketName
     * @param prefix
     * @param recursive  是否使用递归查询
     * @return
     */
    @SneakyThrows
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build());
        if (CollectionUtil.isNotEmpty(results)) {
            for (Result<Item> result : results) {
                list.add(result.get());
            }
        }
        return list;
    }

    /**
     * 获取文件流
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 断点下载
     *
     * @param bucketName
     * @param objectName
     * @param offset     起始字节位置
     * @param length     要读取的长度
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, long length) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }

    /**
     * 获取路径下的文件列表
     *
     * @param bucketName
     * @param prefix
     * @param recursive  是否递归查询，false：模拟文件夹结构查找
     * @return
     */
    public Iterable<Result<Item>> listObject(String bucketName, String prefix, boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
    }

    /**
     * 使用MultipartFile 进行文件上传
     *
     * @param bucketName
     * @param file
     * @param objectName
     * @param contentType 文件类型
     * @return
     */
    @SneakyThrows
    public ObjectWriteResponse uploadFile(String bucketName, MultipartFile file, String objectName, String contentType) {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                .contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * 图片上传
     *
     * @param bucketName
     * @param imageBase64
     * @param imageName
     * @return
     */
    public ObjectWriteResponse uploadImage(String bucketName, String imageBase64, String imageName) {
        if (StringUtils.isNotEmpty(imageBase64)) {
            InputStream in = base64ToInputStream(imageBase64);
            String newName = System.currentTimeMillis() + "_" + imageName + ".jpg";
            String year = String.valueOf(new Date().getYear());
            String month = String.valueOf(new Date().getMonth());
            return uploadFile(bucketName, year + "/" + month + "/" + newName, in);
        }
        return null;
    }

    public static InputStream base64ToInputStream(String base64) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = Base64Decoder.decode(base64.getBytes(StandardCharsets.UTF_8));
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * 上传本地文件
     *
     * @param bucketName
     * @param objectName
     * @param fileName   本地文件路径
     * @return
     */
    @SneakyThrows
    public String uploadFile(String bucketName, String objectName, String fileName) {
        String fileMd5 = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream(fileName)));
        // 创建文件md5名称
        String md5Name = fileMd5 + fileName.substring(fileName.lastIndexOf("."));
        // 判断文件是否已经存在
        MinioFileBo fileInfo = getFileInfo(bucketName, md5Name);
        if (fileInfo != null) {
            return createFilePath(bucketName, md5Name);
        }
        minioClient.uploadObject(
                UploadObjectArgs.builder().bucket(bucketName).object(md5Name)
                        .filename(fileName).build()
        );
        return createFilePath(bucketName, md5Name);
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     * @return
     */
    @SneakyThrows(Exception.class)
    public ObjectWriteResponse uploadFile(String bucketName, String objectName, InputStream inputStream) {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     * @return
     */
    @SneakyThrows(Exception.class)
    public ObjectWriteResponse createDir(String bucketName, String objectName) {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @return
     */
    @SneakyThrows(Exception.class)
    public String getFileStatusInfo(String bucketName, String objectName) {
        StatObjectResponse statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        return statObjectResponse.toString();
    }

    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @return
     */
    public MinioFileBo getFileInfo(String bucketName, String objectName) {
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return MinioFileBo.get(statObjectResponse);
        } catch (Exception e) {
            log.info("文件不存在！{}", objectName);
            return null;
        }
    }

    /**
     * 拷贝文件
     *
     * @param bucketName    存储桶
     * @param objectName    文件名
     * @param srcBucketName 目标存储桶
     * @param srcObjectName 目标文件名
     */
    @SneakyThrows(Exception.class)
    public ObjectWriteResponse copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName) {
        return minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     */
    @SneakyThrows(Exception.class)
    public void removeFile(String bucketName, String objectName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }


    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶
     * @param keys       需要删除的文件列表
     * @return
     */
    public void removeFiles(String bucketName, List<String> keys) {
        List<DeleteObject> objects = new LinkedList<>();
        keys.forEach(s -> {
            objects.add(new DeleteObject(s));
            try {
                removeFile(bucketName, s);
            } catch (Exception e) {
                log.error("[Minio工具类]>>>> 批量删除文件，异常：", e);
            }
        });
    }

    /**
     * 获取文件外链
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().expiry(expires).bucket(bucketName).object(objectName).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 获得文件外链
     *
     * @param bucketName
     * @param objectName
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getPresignedObjectUrl(String bucketName, String objectName) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * 将URLDecoder编码转成UTF8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getUtf8ByURLDecoder(String str) throws UnsupportedEncodingException {
        String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        return URLDecoder.decode(url, Charset.forName("utf-8"));
    }

    private final ThreadPoolTaskConfig threadPoolTaskConfig;

    /**
     * 多线程对已经分片的文件进行上传
     * @param bucketName
     * @param folder
     * @param fileMd5
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public void chunkUpload(String bucketName, String folder, String fileMd5) throws FileNotFoundException, InterruptedException {
        // 获取已经分片的文件夹
        File fileFolder = new File(folder);

        // 判断分片bucket是否存在，不存在就创建
        if (createBucket(fileMd5)) {
            // bucket已经存在，判断分片文件是否已经全部存在
            int fileSize = fileFolder.listFiles().length;
            Iterable<Result<Item>> listObject = listObject(fileMd5, "", false);
            int length=0;
            for (Result<Item> result : listObject) {
                length++;
            }
            if (length == fileSize) {
                deleteFolder(fileFolder);
                // 分片文件已经存在，直接返回
                log.info("分片文件已经存在，直接返回");
                return;
            }
        }
        // 初始化计时器
        CountDownLatch cdl = new CountDownLatch(fileFolder.listFiles().length);
        ThreadPoolTaskExecutor executor = threadPoolTaskConfig.threadPoolTaskExecutor();
        System.out.println("====== 线程开始 =====");
        for (File file : fileFolder.listFiles()) {
            // 开启线程
            executor.execute(() -> {
                try {
                    uploadFile(fileMd5, file.getName(), new FileInputStream(file));
                    System.out.println("上传一个分片");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                // 闭锁-1
                cdl.countDown();
            });
        }
        // 调用闭锁的await()方法，该线程会被挂起，它会等待直到count值为0才继续执行
        // 这样我们就能确保上面多线程都执行完了才走后续代码
        cdl.await();
        //关闭线程池
        executor.shutdown();
        System.out.println("====== 线程结束 =====");
        // 删除分片生成的文件夹
        deleteFolder(fileFolder);
    }

    private static void deleteFolder(File fileFolder) {
        if (fileFolder.isDirectory()) {
            for (File listFile : fileFolder.listFiles()) {
                listFile.delete();
            }
        }
        fileFolder.delete();
    }

    @SneakyThrows
    public void chunkCompose(String bucketName,String md5,String fileName) {
        Iterable<Result<Item>> listObject = listObject(md5, "", false);
        List<String> objectNameList = new ArrayList<>();
        for (Result<Item> result : listObject) {
            Item item = result.get();
            objectNameList.add(item.objectName());
        }
        // 对文件名集合进行升序排序
        objectNameList.sort((o1, o2) -> Integer.parseInt(o2.substring(o2.lastIndexOf("/") + 1, o2.lastIndexOf("."))) > Integer.parseInt(o1.substring(o1.lastIndexOf("/") + 1, o1.lastIndexOf("."))) ? -1 : 1);
        List<ComposeSource> composeSourceList = new ArrayList<>(objectNameList.size());
        //合并文件
        for (String object : objectNameList) {
            composeSourceList.add(ComposeSource.builder()
                    .bucket(md5)
                    .object(object)
                    .build());
        }
        minioClient.composeObject(ComposeObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .sources(composeSourceList)
                .build());
        // 删除分片bucket
        deleteHasFileBucket(md5);
    }

    /**
     * 删除含有文件的bucket
     * 先删除文件，再删除bucket
     * @param bucketName
     */
    @SneakyThrows
    public void deleteHasFileBucket(String bucketName) {
        Iterable<Result<Item>> listObject = listObject(bucketName, "", true);
        List<DeleteObject> removeFiles = new LinkedList<>();
        for (Result<Item> result : listObject) {
            removeFiles.add(new DeleteObject(result.get().objectName()));
        }
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(removeFiles)
                .build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            log.error("Error in deleting object " + error.objectName() + "; " + error.message());
        }
        removeBucket(bucketName);
    }

    /**
     * 分片上传文件 上传合并集合一体
     * @param bucketName
     * @param filePath 文件全路径
     * @param fileName 文件名称
     * @param singleSize 分片大小
     */
    @SneakyThrows
    public String chunkUploadAndCompose(String bucketName,String filePath,String fileName,int singleSize) {
        String fileMd5 = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream(filePath)));
        // 创建文件md5名称
        String md5Name = fileMd5 + fileName.substring(fileName.lastIndexOf("."));
        // 判断文件是否已经存在
        MinioFileBo fileInfo = getFileInfo(bucketName, md5Name);
        if (fileInfo != null) {
            return createFilePath(bucketName, md5Name);
        }
        String folder = FileUtils.cutFile(filePath, fileName, singleSize);
        // 上传
        chunkUpload(bucketName, folder, fileMd5);

        // 合并
        chunkCompose(bucketName, fileMd5, md5Name);
        return createFilePath(bucketName, md5Name);
    }

    private final MinioConfig minioConfig;

    /**
     * 生成文件路径
     * @param bucketName
     * @param fileName
     * @return
     */
    public String createFilePath(String bucketName ,String fileName) {
        return minioConfig.getEndpoint() + "/" + bucketName + "/" + fileName;
    }

    /**
     * 生成文件直传地址
     * @param bucketName
     * @param storeKey
     * @return
     */
    @SneakyThrows
    public String presignedObjectUrl(String bucketName, String storeKey) {
        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("uploadId", uploadId);
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(storeKey)
                .expiry(1, TimeUnit.DAYS)
                .extraQueryParams(queryParams)
                .build());
        return presignedObjectUrl;
    }


}
