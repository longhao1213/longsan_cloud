package com.longsan.dao.mapper;

import com.longsan.domain.bo.UserInfo;
import com.longsan.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author longhao
 * @since 2023/2/7
 */
@Mapper
public interface UserMapper extends BaseMapper {

    UserInfo selectUserByUserName(@Param("userName") String userName);

    Set<String> findUserPermissions(@Param("id") Long id);
}
