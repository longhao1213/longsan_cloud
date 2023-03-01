package com.longsan.common.constant;

/**
 * 多租户常量
 * 
 * @date 2020-9-7
 */
public interface TenantConstant {

    /**
     * header 中租户ID
     */
    String NAITERUI_TENANT_ID = "naiterui-tenant";

    /**
     * 租户id参数
     */
    String NAITERUI_TENANT_ID_PARAM = "tenantId";

    /**
     * 租户ID
     */
    Long TENANT_ID_DEFAULT = 1L;

}
