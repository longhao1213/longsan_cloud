package com.longsan.mybatis;

import java.util.List;

//@Data
//@Component("mybatisTenantProperties")
//@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 需要进行租户解析的租户表
     */
    private List<String> tables;
}