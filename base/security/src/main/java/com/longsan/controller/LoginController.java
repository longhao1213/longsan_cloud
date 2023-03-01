package com.longsan.controller;

import com.longsan.common.api.Result;
import com.longsan.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longhao
 * @since 2023/2/7
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(String userName, String password) {
        String token = loginService.login(userName, password);
        return Result.data(token);
    }

}
