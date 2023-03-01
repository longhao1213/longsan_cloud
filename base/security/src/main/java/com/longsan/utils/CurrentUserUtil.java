package com.longsan.utils;

import com.longsan.common.exception.BaseException;
import com.longsan.domain.bo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Slf4j
public class CurrentUserUtil {

    private static final ThreadLocal<LoginUser> userHolder = new ThreadLocal<>();


    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @Nullable
    public static LoginUser getUser() {
        LoginUser user = userHolder.get();
        if (user == null) {
            log.warn("获取当前登录用户失败");
        }
        return user;
    }

    /**
     * 获取用户id 允许id为空
     *
     * @return
     */
    @Nullable
    public static Long getUserId() {
        return getUser() == null ? null : getUser().getUserInfo().getUserId();
    }

    /**
     * 获取当前登录用户名 允许为空
     *
     * @return
     */
    public static String getUsername() {
        return getUser() == null ? "系统" : getUser().getUserInfo().getUserName();
    }


    /**
     * 获取当前登录用户id，不允许id为空
     *
     * @return
     */
    public static Long getUserIdNotNull() {
        if (userHolder.get().getUserInfo().getUserId() == null) {
            throw new BaseException("当前登录用户id为空");
        }
        return userHolder.get().getUserInfo().getUserId();
    }

    /**
     * 获取当前登录用户名，不允许为空
     *
     * @return
     */
    public static String getUsernameNotNull() {
        if (StringUtils.isBlank(getUser().getUsername())) {
            throw new BaseException("当前登录用户名为空");
        }
        return userHolder.get().getUsername();
    }

    /**
     * 获取当前用户权限信息
     * @return
     */
    public static Set<String> getPermissions() {
        return userHolder.get().getPermissions();
    }

    public static void setUser(LoginUser loginUser) {
        if (loginUser == null) {
            throw new BaseException("登录用户信息不存在");
        }
        userHolder.set(loginUser);
    }
}
