package com.longsan.handler;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.longsan.domain.bo.LoginUser;
import com.longsan.utils.CurrentUserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Slf4j
@Component(value = "el")
@AllArgsConstructor
public class PreAuthHandler {

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    private final HttpServletRequest request;

    /**
     * 验证用户是否具备某种权限
     * @param permission 权限字符串
     * @return
     */
    public boolean check(String... permission) {
        LoginUser user = CurrentUserUtil.getUser();
        if (ObjectUtil.isEmpty(user)) {
            return false;
        }

        Set<String> permissions = user.getPermissions();

        return hasPermissions(Lists.newArrayList(permissions), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permissions 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Collection<String> authorities, String... permissions) {
        return Arrays.asList(permissions).contains(ALL_PERMISSION) || Arrays.stream(permissions).anyMatch(authorities::contains);
    }
}
