import com.longsan.minio.MinioApplication;
import com.longsan.minio.utils.MinioFileBo;
import com.longsan.minio.utils.MinioUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author longhao
 * @since 2023/3/6
 */
@SpringBootTest(classes = MinioApplication.class)
public class MinioTest {

    @Autowired
    private MinioUtils minioUtils;

    @Test
    public void test1() {
        String test = minioUtils.getFileStatusInfo("test", "1677746688227.jpg");
        System.out.println(test);
        MinioFileBo fileInfo = minioUtils.getFileInfo("test", "1677746688227.jpg");
        System.out.println(fileInfo);
    }


}
