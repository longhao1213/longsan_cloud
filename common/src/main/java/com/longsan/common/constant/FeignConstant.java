package com.longsan.common.constant;

import lombok.experimental.UtilityClass;

/**
 * Feign常量类
 */
@UtilityClass
public class FeignConstant {

    /**
     * 网关
     */
    public final String GATEWAY = "netrain-gateway";

    /**
     * 平台基础服务
     */
    public final String PLATFORM = "netrain-platform";

    /**
     * His业务服务
     */
    public final String HIS = "netrain-his";

    /**
     * 管理端服务
     */
    public final String TENANT = "netrain-tenant";

    /**
     * 仓库管理服务
     */
    public final String WMS = "netrain-wms";

    public final String SCHEDULING = "outpatient-scheduling";

    public final String FEE = "fee-management";

    /**
     * 门诊管理端服务
     */
    public final String OUTPATIENT_MANAGEMENT = "outpatient-management";

    /**
     * XXXX服务
     */
    public final String XXXX = "netrain-xxxx";
}
