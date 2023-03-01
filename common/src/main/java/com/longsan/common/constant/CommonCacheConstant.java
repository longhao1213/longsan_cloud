package com.longsan.common.constant;

/**
 * 公共缓存前缀
 *
 * @author guoyongxiang
 * @version 1.0.0
 * @date 2021/11/4 16:59
 **/
public interface CommonCacheConstant {

    /**
     * redis key 前缀 platform 服务
     */
    String RDS_PLATFORM = "platform:";

    /**
     * redis key 前缀 tenant 服务
     */
    String RDS_TENANT = "tenant:";

    /**
     * redis key 前缀 outpatient_management 服务
     */
    String RDS_OUTPATIENT_MANAGEMENT = "outpatient_management:";

    /**
     * redis key 前缀 outpatient_scheduling 服务
     */
    String RDS_OUTPATIENT_SCHEDULING = "outpatient_scheduling:";
}
