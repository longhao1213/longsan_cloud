package com.longsan.service.Impl;

import com.longsan.dao.mapper.UserMapper;
import com.longsan.domain.bo.LoginUser;
import com.longsan.domain.bo.UserInfo;
import com.longsan.service.UserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Service
public class UserDetailServiceImpl implements UserDetailService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userMapper.selectUserByUserName(username);
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 获取用户权限
        Set<String> permissions = userMapper.findUserPermissions(userInfo.getUserId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserInfo(userInfo);
        loginUser.setPermissions(permissions);
        return loginUser;
    }
}
