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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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


    /**************** bucket?????? *************/

    /**
     * ??????bucket
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
     * ??????bucket??????
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
     * ??????bucket????????????
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    private boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * ??????bucket??????
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
     * ????????????bucket??????
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }


    /**
     * ??????bucket????????????
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return getAllBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * ??????bucket
     *
     * @param bucketName
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**************** file?????? *************/


    /**
     * ????????????????????????
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
            log.error("[Minio?????????]>>>> ????????????????????????, ?????????", e);
            exist = false;
        }
        return exist;
    }

    /**
     * ???????????????????????????
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
            log.error("[Minio?????????]>>>> ???????????????????????????????????????", e);
            exist = false;
        }
        return exist;
    }

    /**
     * ??????????????????????????????
     *
     * @param bucketName
     * @param prefix
     * @param recursive  ????????????????????????
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
     * ???????????????
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
     * ????????????
     *
     * @param bucketName
     * @param objectName
     * @param offset     ??????????????????
     * @param length     ??????????????????
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, long length) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }

    /**
     * ??????????????????????????????
     *
     * @param bucketName
     * @param prefix
     * @param recursive  ?????????????????????false??????????????????????????????
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
     * ??????MultipartFile ??????????????????
     *
     * @param bucketName
     * @param file
     * @param objectName
     * @param contentType ????????????
     * @return
     */
    @SneakyThrows
    public ObjectWriteResponse uploadFile(String bucketName, MultipartFile file, String objectName, String contentType) {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
                .contentType(contentType).stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * ????????????
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
     * ??????????????????
     *
     * @param bucketName
     * @param objectName
     * @param fileName   ??????????????????
     * @return
     */
    @SneakyThrows
    public String uploadFile(String bucketName, String objectName, String fileName) {
        String fileMd5 = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream(fileName)));
        // ????????????md5??????
        String md5Name = fileMd5 + fileName.substring(fileName.lastIndexOf("."));
        // ??????????????????????????????
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
     * ?????????????????????
     *
     * @param bucketName  ?????????
     * @param objectName  ????????????
     * @param inputStream ?????????
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
     * ????????????????????????
     *
     * @param bucketName ?????????
     * @param objectName ????????????
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
     * ??????????????????, ??????????????????????????????????????????
     *
     * @param bucketName ?????????
     * @param objectName ????????????
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
     * ??????????????????, ??????????????????????????????????????????
     *
     * @param bucketName ?????????
     * @param objectName ????????????
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
            log.info("??????????????????{}", objectName);
            return null;
        }
    }

    /**
     * ????????????
     *
     * @param bucketName    ?????????
     * @param objectName    ?????????
     * @param srcBucketName ???????????????
     * @param srcObjectName ???????????????
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
     * ????????????
     *
     * @param bucketName ?????????
     * @param objectName ????????????
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
     * ??????????????????
     *
     * @param bucketName ?????????
     * @param keys       ???????????????????????????
     * @return
     */
    public void removeFiles(String bucketName, List<String> keys) {
        List<DeleteObject> objects = new LinkedList<>();
        keys.forEach(s -> {
            objects.add(new DeleteObject(s));
            try {
                removeFile(bucketName, s);
            } catch (Exception e) {
                log.error("[Minio?????????]>>>> ??????????????????????????????", e);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param bucketName ?????????
     * @param objectName ?????????
     * @param expires    ???????????? <=7 ??? ??????????????????????????????????????????
     * @return url
     */
    @SneakyThrows(Exception.class)
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().expiry(expires).bucket(bucketName).object(objectName).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    /**
     * ??????????????????
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
     * ???URLDecoder????????????UTF8
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
     * ?????????????????????????????????????????????
     * @param bucketName
     * @param folder
     * @param fileMd5
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public void chunkUpload(String bucketName, String folder, String fileMd5) throws FileNotFoundException, InterruptedException {
        // ??????????????????????????????
        File fileFolder = new File(folder);

        // ????????????bucket?????????????????????????????????
        if (createBucket(fileMd5)) {
            // bucket?????????????????????????????????????????????????????????
            int fileSize = fileFolder.listFiles().length;
            Iterable<Result<Item>> listObject = listObject(fileMd5, "", false);
            int length=0;
            for (Result<Item> result : listObject) {
                length++;
            }
            if (length == fileSize) {
                deleteFolder(fileFolder);
                // ???????????????????????????????????????
                log.info("???????????????????????????????????????");
                return;
            }
        }
        // ??????????????????
        CountDownLatch cdl = new CountDownLatch(fileFolder.listFiles().length);
        ThreadPoolTaskExecutor executor = threadPoolTaskConfig.threadPoolTaskExecutor();
        System.out.println("====== ???????????? =====");
        for (File file : fileFolder.listFiles()) {
            // ????????????
            executor.execute(() -> {
                try {
                    uploadFile(fileMd5, file.getName(), new FileInputStream(file));
                    System.out.println("??????????????????");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                // ??????-1
                cdl.countDown();
            });
        }
        // ???????????????await()???????????????????????????????????????????????????count??????0???????????????
        // ????????????????????????????????????????????????????????????????????????
        cdl.await();
        //???????????????
        executor.shutdown();
        System.out.println("====== ???????????? =====");
        // ??????????????????????????????
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
        // ????????????????????????????????????
        objectNameList.sort((o1, o2) -> Integer.parseInt(o2.substring(o2.lastIndexOf("/") + 1, o2.lastIndexOf("."))) > Integer.parseInt(o1.substring(o1.lastIndexOf("/") + 1, o1.lastIndexOf("."))) ? -1 : 1);
        List<ComposeSource> composeSourceList = new ArrayList<>(objectNameList.size());
        //????????????
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
        // ????????????bucket
        deleteHasFileBucket(md5);
    }

    /**
     * ?????????????????????bucket
     * ???????????????????????????bucket
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
     * ?????????????????? ????????????????????????
     * @param bucketName
     * @param filePath ???????????????
     * @param fileName ????????????
     * @param singleSize ????????????
     */
    @SneakyThrows
    public String chunkUploadAndCompose(String bucketName,String filePath,String fileName,int singleSize) {
        String fileMd5 = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream(filePath)));
        // ????????????md5??????
        String md5Name = fileMd5 + fileName.substring(fileName.lastIndexOf("."));
        // ??????????????????????????????
        MinioFileBo fileInfo = getFileInfo(bucketName, md5Name);
        if (fileInfo != null) {
            return createFilePath(bucketName, md5Name);
        }
        String folder = FileUtils.cutFile(filePath, fileName, singleSize);
        // ??????
        chunkUpload(bucketName, folder, fileMd5);

        // ??????
        chunkCompose(bucketName, fileMd5, md5Name);
        return createFilePath(bucketName, md5Name);
    }

    private final MinioConfig minioConfig;

    public String createFilePath(String bucketName ,String fileName) {
        return minioConfig.getEndpoint() + "/" + bucketName + "/" + fileName;
    }


}
