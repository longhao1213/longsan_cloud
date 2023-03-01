package com.longsan.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author longhao
 * @since 2021/10/29
 */
@Configuration
public class MybatisConfig {
//
//    @Resource(name = "mybatisTenantProperties")
//    private TenantProperties tenantProperties;

    /**
     * 注册乐观锁插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        // 添加多租户插件
//        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
//            /**
//             * 设置租户id
//             * @returnF
//             */
//            @Override
//            public Expression getTenantId() {
//                return new LongValue(SecurityUtil.getTenantId() == null ? -1 : SecurityUtil.getTenantId());
//            }
//
//            /**
//             * 设置表字段 默认tenant_id
//             * @return
//             */
//            @Override
//            public String getTenantIdColumn() {
//                return TenantLineHandler.super.getTenantIdColumn();
//            }
//
//            /**
//             * 过滤表名 如果不需要拦截就添加返回true
//             *
//             * @InterceptorIgnore(tenantLine = "true")  在mapper方法上加上，可以指定这个查询不拦截
//             * @param tableName
//             * @return
//             */
//            @Override
//            public boolean ignoreTable(String tableName) {
//                return !tenantProperties.getTables().contains(tableName);
//            }
//        }));
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    public com.longsan.mybatis.EasySqlInjector easySqlInjector() {
        return new com.longsan.mybatis.EasySqlInjector();
    }
}
