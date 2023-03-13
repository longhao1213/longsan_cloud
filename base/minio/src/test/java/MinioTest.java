import com.longsan.minio.MinioApplication;
import com.longsan.minio.utils.FileUtils;
import com.longsan.minio.utils.MinioFileBo;
import com.longsan.minio.utils.MinioUtils;
import io.minio.MinioClient;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author longhao
 * @since 2023/3/6
 */
@SpringBootTest(classes = MinioApplication.class)
public class MinioTest {

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private MinioClient minioClient;

    @Test
    public void test1() {
        String test = minioUtils.getFileStatusInfo("test", "1677746688227.jpg");
        System.out.println(test);
        MinioFileBo fileInfo = minioUtils.getFileInfo("test", "1677746688227.jpg");
        System.out.println(fileInfo);
    }

    @Test
    public void test2() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InterruptedException {
        String path = FileUtils.cutFile("/Users/longhao/temp/test.rvt", "test.rvt", 5 * 1024 * 1024);
        String md5 = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream("/Users/longhao/temp/test.rvt")));
        minioUtils.chunkUpload("test",path, md5);
//        minioUtils.chunkCompose("test", md5, "test.rvt");

    }

    @Test
    @SneakyThrows
    public void test3() {
        Iterable<Result<Item>> listObject = minioUtils.listObject("1d2c27385221bfe7d0d4898df322157b", "", true);
        List<DeleteObject> removeFiles = new LinkedList<>();
        for (Result<Item> result : listObject) {
            removeFiles.add(new DeleteObject(result.get().objectName()));
        }
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket("1d2c27385221bfe7d0d4898df322157b")
                .objects(removeFiles)
                .build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            System.out.println(
                    "Error in deleting object " + error.objectName() + "; " + error.message());
        }
        minioUtils.removeBucket("1d2c27385221bfe7d0d4898df322157b");
    }

    @Test
    public void test4() {
        String path = minioUtils.chunkUploadAndCompose("test", "/Users/longhao/temp/test.rvt", "test.rvt", 5 * 1024 * 1024);
        System.out.println(path);
    }

    @Test
    public void test5() {
        String test = minioUtils.uploadFile("test", "1.jpg","/Users/longhao/temp/1.jpg");
        System.out.println(test);
    }

    @Test
    public void test6() {
        minioUtils.removeBucket("test");
    }

    public static void main(String[] args) {
        File file = new File("/Users/longhao/temp/test");
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                listFile.delete();
            }
        }
        boolean delete = file.delete();
        System.out.println(delete);
    }


}
