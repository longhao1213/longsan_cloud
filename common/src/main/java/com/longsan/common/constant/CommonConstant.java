package com.longsan.common.constant;

import lombok.experimental.UtilityClass;

/**
 * 公共基本常量
 *
 */
@UtilityClass
public class CommonConstant {

    /**
     * 应用版本号
     */
    public static final String APP_VERSION = "0.0.1-SNAPSHOT";

    /**
     * Spring 应用名 prop key
     */
    public static final String SPRING_APP_NAME_KEY = "spring.application.name";

    /**
     * 默认为空消息
     */
    public static final String DEFAULT_NULL_MESSAGE = "承载数据为空";
    /**
     * 默认成功消息
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "处理成功";
    /**
     * 默认失败消息
     */
    public static final String DEFAULT_FAIL_MESSAGE = "处理失败";
    /**
     * 树的根节点值
     */
    public static final Long TREE_ROOT = -1L;
    /**
     * 允许的文件类型，可根据需求添加
     */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 微服务之间传递的唯一标识
     */
    public static final String SERVER_TRACE_ID = "server-trace-id";

    /**
     * 日志链路追踪id日志标志
     */
    public static final String LOG_TRACE_ID = "traceId";

    /**
     * Java默认临时目录
     */
    public static final String JAVA_TEMP_DIR = "java.io.tmpdir";

    /**
     * 版本
     */
    public static final String VERSION = "version";

    /**
     * 默认版本号
     */
    public static final String DEFAULT_VERSION = "v1";

    /**
     * 服务资源
     */
    public static final String SERVICE_RESOURCE = "naiterui-service-resource";

    /**
     * API资源
     */
    public static final String NAITERUI_API_RESOURCE = "naiterui-api-resource";

    /**
     * 权限认证的类加载顺序
     */
    public static final int AUTH_FILTER_ORDER = -200;

    /**
     * 签名排序
     */
    public static final int SIGN_FILTER_ORDER = -300;

    /**
     * json类型报文，UTF-8字符集
     */
    public static final String JSON_UTF8 = "application/json;charset=UTF-8";

    public static final String CONFIG_GROUP = "DEFAULT_GROUP";
    public static final long CONFIG_TIMEOUT_MS = 5000;

    /**
     * 动态路由配置中心dataId
     */
    public static final String CONFIG_DATA_ID_DYNAMIC_ROUTES = "dynamic-routes.yaml";

    /**
     * 网关管理服务端前缀集合，统一auth鉴权去除改前缀
     */
    public static final String DYNAMIC_ROUTES_PREFIX_CACHE = "dynamic-routes-cache";
}
