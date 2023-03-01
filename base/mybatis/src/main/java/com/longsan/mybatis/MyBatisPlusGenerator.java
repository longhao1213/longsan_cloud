package com.longsan.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        // 是否支持AR模式
        config.setActiveRecord(true)
                // 作者
                .setAuthor("longhao")
                // 生成路径，最好使用绝对路径，window路径是不一样的
                //TODO  TODO  TODO  TODO
                .setOutputDir("/Users/longhao/temp/demo/src/main/java")
                // 文件覆盖
                .setFileOverride(true)
                // 主键策略 默认雪花算法
                .setIdType(IdType.ASSIGN_ID)

                .setDateType(DateType.ONLY_DATE)
                // 设置生成的service接口的名字的首字母是否为I，默认Service是以I开头的
                .setServiceName("%sService")

                //实体类结尾名称
//                .setEntityName("%")

                //生成基本的resultMap
                .setBaseResultMap(true)

                //不使用AR模式
                .setActiveRecord(false)

                //生成基本的SQL片段
                .setBaseColumnList(true)
                //开启swagger支持
                .setSwagger2(true);

        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        // 设置数据库类型
        dsConfig.setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.cj.jdbc.Driver")
                //TODO  TODO  TODO  TODO
                .setUrl("jdbc:mysql://127.0.0.1:3306/lh_cloud?useSSL=false")
                .setUsername("root")
                .setPassword("123456");

        //3. 策略配置globalConfiguration中
        StrategyConfig stConfig = new StrategyConfig();

        //全局大写命名
        stConfig.setCapitalMode(true)

                // 所有entity继承这个类
//                .setSuperEntityClass(BaseEntity.class)

                // 所有的mapper继承这个类
                .setSuperMapperClass("com.longsan.mybatis.BaseMapper")

                // 数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)

                //使用lombok插件
                .setEntityLombokModel(true)

                //使用restcontroller注解
                .setRestControllerStyle(true)


                //TODO  TODO  TODO  TODO
                // 去除表前缀
                .setTablePrefix("")
                // 生成的表, 支持多表一起生成，以数组形式填写
                .setInclude("sys_menu","sys_user","sys_role");

        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        // TODO
        pkConfig.setParent("com.longsan")
                .setMapper("dao.mapper")
                .setService("service")
                .setController("controller")
                .setEntity("domain.entity")
                .setXml("mapper");

        //5. 整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);

        //6. 执行操作
        ag.execute();
        System.out.println("======= 龙三 Done 相关代码生成完毕  ========");
    }

}