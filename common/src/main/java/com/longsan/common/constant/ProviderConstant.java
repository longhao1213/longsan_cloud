package com.longsan.common.constant;

/**
 * 用户常量
 */
public interface ProviderConstant {
    /**
     * 远程调用公共前缀
     */
    String PROVIDER = "/inner";

    /**
     * 根据id查询用户信息
     */
    String PROVIDER_USER_ID = PROVIDER + "/user/id";

    /**
     * 根据username查询用户信息
     */
    String PROVIDER_USER_USERNAME = PROVIDER + "/user/username";

    /**
     * 根据手机号查询用户信息
     */
    String PROVIDER_USER_PHONE = PROVIDER + "/user/phone";

    /**
     * 根据用户ID查询租户列表
     */
    String PROVIDER_USER_TENANTS = PROVIDER + "/user/tenants";

    /**
     * 根据roleId查询menuId列表
     */
    String PROVIDER_ROLE_MENU_COMPONENT = PROVIDER + "/role-permission/permission/component";

    /**
     * 根据roleId查询菜单权限标识符
     */
    String PROVIDER_ROLE_MENU_PERMISSION = PROVIDER + "/role-permission/permission";

    /**
     * 根据userId查询菜单权限标识符
     */
    String PROVIDER_USER_MENU_PERMISSION = PROVIDER + "/user-permission/permission";


    /**
     * 查询城市字典值
     */
    String PROVIDER_DICT_AREA = PROVIDER + "/dict/area";

    /**
     * 支付类型字典查询
     */
    String DICT_PAY_MODE = PROVIDER + "/dict/pay/mode";


    /**
     * 查询机构科室
     */
    String PROVIDER_TENANT_DEPT = PROVIDER + "/tenant/dept";


    /**
     * 查询机构配置
     */
    String PROVIDER_TENANT_CONF = PROVIDER + "/tenant/conf";

    /**
     * 查询机构配置
     */
    String PROVIDER_TENANT_CONF_LIST = PROVIDER + "/tenant/conf/list";

    /**
     *
     */
    String PROVIDER_TENANT_MENU = PROVIDER + "/tenant/menus";

    /**
     * 查询字典值
     */
    String PROVIDER_DICT_VALUE = PROVIDER + "/dict/value";

    /**
     * 查询字典列表
     */
    String PROVIDER_DICT_LIST = PROVIDER + "/dict/list";

    /**
     * 日志配置
     */
    String PROVIDER_LOG_SET = PROVIDER + "/log/set";

    /**
     * 机构人员
     */
    String PROVIDER_TENANT_PERSON = PROVIDER + "/tenant/person";

    /**
     * 根据ids查询字典集合
     */
    String PROVIDER_DICT_UNIT_LIST = PROVIDER + "/dict/unit/listByIds";

    /**
     * 根据项目前缀获取项目代码
     */
    String PROVIDER_CODE_PROJECT_PREFIX = PROVIDER + "/project/code";

    /**
     * 根据项目字典id获取名称
     */
    String PROVIDER_CODE_PROJECT_NAME = PROVIDER + "/project/code/dict";
}
