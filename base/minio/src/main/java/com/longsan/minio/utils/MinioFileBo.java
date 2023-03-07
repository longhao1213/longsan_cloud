package com.longsan.minio.utils;

import io.minio.StatObjectResponse;
import lombok.Data;
import lombok.ToString;

/**
 * @author longhao
 * @since 2023/3/6
 */
@Data
@ToString
public class MinioFileBo {

    private String bucketName;

    private Long size;

    private String fileName;

    public static MinioFileBo get(StatObjectResponse response) {
        MinioFileBo minioFileBo = new MinioFileBo();
        minioFileBo.setBucketName(response.bucket());
        minioFileBo.setFileName(response.object());
        minioFileBo.setSize(response.size());
        return minioFileBo;
    }
}
