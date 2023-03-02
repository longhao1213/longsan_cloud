import com.longsan.es.EsApplication;
import com.longsan.es.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longhao
 * @since 2023/3/1
 */
@SpringBootTest(classes = EsApplication.class)
public class EsTest {


    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 初始化索引
     */
    @Test
    public void createIndex() {
        IndexOperations indexOperations = restTemplate.indexOps(User.class);
        if (indexOperations.exists()) {
            indexOperations.delete();
        }
        // 创建索引
        indexOperations.create();
        // 创建mapping
        Document mapping = indexOperations.createMapping(User.class);
        indexOperations.putMapping(mapping);
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        IndexOperations indexOperations = restTemplate.indexOps(User.class);
        boolean delete = indexOperations.delete();
        System.out.println(delete);
    }

    /**
     * 新增单个文档
     */
    @Test
    public void addOne() {
        User student = new User();
        student.setAge(23);
        student.setData("123");
        student.setDesc("华为手机");
        student.setId("1");
        student.setName("张三");
        User student2 = new User();
        student2.setAge(12);
        student2.setData("456");
        student2.setDesc("xiaomi手机");
        student2.setId("2");
        student2.setName("里斯");
        User save = restTemplate.save(student);
        restTemplate.save(student2);
        System.out.println(save);
    }


    /**
     * 批量添加
     */
    @Test
    void batchInsert() {
        List<User> list = new ArrayList<>();
        list.add(new User("6","李四","苹果手机","1",22));
        list.add(new User("3","王五","oppo手机","2",24));
        list.add(new User("4","赵六","voio手机","3",25));
        list.add(new User("5","田七","小米手机","4",26));
        Iterable<User> save = restTemplate.save(list);
        System.out.println(save);
    }

    /**
     * 根据主键删除
     */
    @Test
    void deleteById() {
        // 根据ID删除
        String delete = restTemplate.delete("1", User.class);
        System.out.println(delete);
    }

    /**
     * 根据条件删除
     */
    @Test
    public void delete() {
        NativeSearchQuery query = new NativeSearchQuery(QueryBuilders.queryStringQuery("里斯"));
        String delete = restTemplate.delete(query);
        System.out.println(delete);
    }

    /**
     * 全量修改
     */
    @Test
    void update() {
        User user = new User("4", "赵六1", "voio手机1", "31", 45);
        User save = restTemplate.save(user);
        System.out.println(save);
    }

    /**
     * 部分修改
     */
    @Test
    void update2() {
        // ctx.source固定写法
        String script = "ctx.source.age = 98;ctx._source.desc = 'oppo手机and苹果电脑'";
        UpdateQuery build = UpdateQuery.builder("3").withScript(script).build();
        IndexCoordinates userInfo = IndexCoordinates.of("user_info");
        UpdateResponse update = restTemplate.update(build, userInfo);
        System.out.println(update);
    }

    /**
     * 根据ID查询 和 条件查询
     */
    @Test
    public void searchByIdAndLike() {
        User user = restTemplate.get("1", User.class);
        System.out.println(user);

        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("手机");
        Query query = new NativeSearchQuery(queryBuilder);
        SearchHits<User> search = restTemplate.search(query, User.class);
        for (SearchHit<User> searchHit : search.getSearchHits()) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * 使用match_all查询所有文档
     */
    @Test
    void matchAllSearch(){
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        Query query = new NativeSearchQuery(queryBuilder);
        SearchHits<User> search = restTemplate.search(query, User.class);
        List<SearchHit<User>> searchHits = search.getSearchHits();
        List<User> list = new ArrayList<>();
        searchHits.forEach(sh->{
            list.add(sh.getContent());
        });
        System.out.println(list);
    }

    /**
     * 分页和排序查询
     */
    @Test
    public void pageSearch() {
        Query query = new NativeSearchQuery(QueryBuilders.matchAllQuery());
        // 排序
        query.addSort(Sort.by(Sort.Direction.DESC, "age"));
        // 分页
        query.setPageable(PageRequest.of(1, 2));
        SearchHits<User> search = restTemplate.search(query, User.class);
        System.out.println(search.getTotalHits());
        search.getSearchHits().forEach(list->{
            System.out.println(list.getContent());
        });
    }

    /**
     * 多条件查询
     */
    @Test
    public void querysSearch() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> list = new ArrayList<>();
        list.add(QueryBuilders.matchQuery("name","张三"));
        list.add(QueryBuilders.matchQuery("age","23"));
        boolQueryBuilder.must().addAll(list);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        SearchHits<User> search = restTemplate.search(nativeSearchQuery, User.class);
        search.getSearchHits().forEach(t->{
            System.out.println(t);
        });
    }

}
