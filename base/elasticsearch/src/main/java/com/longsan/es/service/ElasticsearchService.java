package com.longsan.es.service;

import com.longsan.es.User;

/**
 * @author longhao
 * @since 2023/3/1
 */
public interface ElasticsearchService {

    void save(User user);

    User find();
}
