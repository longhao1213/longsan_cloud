/*
 * Copyright 2019-2028 Beijing Daotiandi Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. Author: xuzhanfu (7333791@qq.com)
 */
package com.longsan.common.constant;

/**
 * 认证URL常量
 **/
public class Oauth2Constant {

    public static final String ALL = "/**";

    public static final String OAUTH_ALL = "/oauth/**";

    public static final String OAUTH_AUTHORIZE = "/oauth/authorize";

    public static final String OAUTH_CHECK_TOKEN = "/oauth/check_token";

    public static final String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

    public static final String OAUTH_TOKEN = "/oauth/token";

    public static final String OAUTH_TOKEN_KEY = "/oauth/token_key";

    public static final String OAUTH_ERROR = "/oauth/error";

    public static final String OAUTH_MOBILE = "/oauth/mobile";

    public static final String OAUTH_ACCESSTOKEN = "access_token";
    public static final String OAUTH_SUBMITTOKEN = "submit_token";
    public static final String OAUTH_TENANT_SUBMIT = "tenant.submit";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

    /**
     * 社交登录，传递的参数名称
     */
    public static final String DEFAULT_PARAMETER_NAME_SOCIAL = "social";

    /**
     * 验证码 key
     */
    public static final String VALIDATE_CODE_KEY = "key";
    /**
     * 验证码 code
     */
    public static final String VALIDATE_CODE_CODE = "code";
    /**
     * 认证类型参数 key
     */
    public static final String GRANT_TYPE = "grant_type";
    /**
     * 登录类型
     */
    public static final String LOGIN_TYPE = "login_type";

    /**
     * 刷新模式
     */
    public static final String REFRESH_TOKEN = "refresh_token";
    /**
     * 授权码模式
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";
    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    /**
     * 密码模式
     */
    public static final String PASSWORD = "password";
    /**
     * 简化模式
     */
    public static final String IMPLICIT = "implicit";

    public static final String SIGN_KEY = "NETRAIN";

    public static final String RDS_CAPTCHA_IMG_KEY = "netrain.captcha.";

    public static final String RDS_CAPTCHA_SMS_KEY = "netrain.sms.code.";

    public static final String CAPTCHA_IMG_HEADER_KEY = "uuid";
    public static final String CAPTCHA_SMS_HEADER_KEY = "phone";

    public static final String CAPTCHA_IMG_HEADER_CODE = "code";
    public static final String CAPTCHA_SMS_HEADER_CODE = "smsCode";

    public static final int LOGIN_USERNAME_TYPE = 1;

    public static final int LOGIN_MOBILE_TYPE = 2;

    public static final String AUTH_TOKEN = "Authorization";

    public static final String HEADER_TOKEN = "naiterui-auth";

    /**
     * 自定义client表名
     */
    public static final String CLIENT_TABLE = "netrain_sys_client";

    public static final String PLATFROM_CLIENT = "netrain";

    public static final String CAPTCHA_ERROR = "验证码不正确！";

    public static final String SUPER_ADMIN = "admin";

    /**
     * 基础查询语句
     */
    public static final String CLIENT_BASE =
        "select client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,"
            + "refresh_token_validity, additional_information, autoapprove from " + CLIENT_TABLE;

    public static final String FIND_CLIENT_DETAIL_SQL = CLIENT_BASE + " order by client_id";

    public static final String SELECT_CLIENT_DETAIL_SQL = CLIENT_BASE + " where client_id = ?";

    /**
     * 标志
     */
    public static final String FROM = "from";

    /**
     * 内部
     */
    public static final String FROM_IN = "Y";

    /**
     * 字段描述开始：用户ID
     */
    public static final String USER_ID = "userId";

    /**
     * 用户(账户)名
     */
    public static final String USERNAME = "username";

    /**
     * 用户姓名
     */
    public static final String NICK_NAME = "nickName";

    /**
     * 用户头像
     */
    public static final String USER_AVATAR = "avatar";

    /**
     * 用户权限ID
     */
    public static final String USER_ROLE_ID = "roleId";

    /**
     * 登录类型
     */
    public static final String TYPE = "type";
    /**
     * 用户账户类型
     */
    public static final String USER_TYPE = "userType";
    /**
     * 绑定租户ID列表
     */
    public static final String USER_CURRENT_TENANT = "currentTenant";
    /**
     * 绑定租户ID列表
     */
    public static final String USER_TENANTS = "tenants";

}
