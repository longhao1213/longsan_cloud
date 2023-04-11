package com.longsan.service.impl;

import com.longsan.domain.entity.SysUser;
import com.longsan.dao.mapper.SysUserMapper;
import com.longsan.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2023-04-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
