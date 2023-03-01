package com.longsan.es.service;

import com.longsan.es.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.stereotype.Service;

/**
 * @author longhao
 * @since 2023/3/1
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService{

    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    public void save(User user) {
        User save = template.save(user);
//        repository.save(user);
    }

    @Override
    public User find() {
        BaseQuery query = new BaseQuery();
        query.addFields("张三");
        SearchHits<User> search = template.search(query, User.class);
        for (SearchHit<User> searchHit : search.getSearchHits()) {
            User content = searchHit.getContent();
            System.out.println(content.getName());
        }
        return null;
    }
}
