package com.longsan.service;

import cn.hutool.core.util.IdUtil;
import com.longsan.constant.Constants;
import com.longsan.domain.bo.LoginUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Service
public class TokenService {

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;


    public String createToken(LoginUser loginUser) {
        String token = IdUtil.fastUUID();
        loginUser.setToken(token);

        // 生成jwt的token
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }



}
