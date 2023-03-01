package com.longsan.service;

/**
 * @author longhao
 * @since 2023/2/7
 */
public interface LoginService {

    /**
     * 用户名密码登陆
     * @param userName
     * @param password
     * @return
     */
    String login(String userName, String password);
}
