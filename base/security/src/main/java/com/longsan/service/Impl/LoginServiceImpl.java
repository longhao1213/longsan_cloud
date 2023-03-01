package com.longsan.service.Impl;

import com.longsan.domain.bo.LoginUser;
import com.longsan.service.LoginService;
import com.longsan.service.TokenService;
import com.longsan.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();


    @Override
    public String login(String userName, String password) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (Exception e) {
            log.error("登陆失败，失败原因：{}",e.getMessage());
            e.printStackTrace();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        CurrentUserUtil.setUser(loginUser);
        return tokenService.createToken(loginUser);
    }

}
