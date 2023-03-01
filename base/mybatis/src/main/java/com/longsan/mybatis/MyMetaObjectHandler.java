package com.longsan.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author longhao
 * @since 2021/10/28
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }

//    @Override
//    public void insertFill(MetaObject metaObject) {
//        CurrentUser currentUser = UserLoginCheckInterceptor.threadLocal.get();
//        if (ObjectUtil.isEmpty(metaObject.getValue("createBy")) && currentUser != null) {
//            this.setFieldValByName("createBy", currentUser.getUserName(), metaObject);
//        }
//        if (metaObject.hasSetter("tenantId") && ObjectUtil.isEmpty(metaObject.getValue("tenantId"))) {
////            this.setFieldValByName("tenantId", SecurityUtil.getTenantId(), metaObject);
//        }
//        if (metaObject.hasSetter("deleted") && ObjectUtil.isEmpty(metaObject.getValue("deleted")) && currentUser != null) {
//            this.setFieldValByName("deleted", Boolean.FALSE, metaObject);
//        }
//        if (ObjectUtil.isEmpty(metaObject.getValue("updateBy")) && currentUser != null) {
//            this.setFieldValByName("updateBy", currentUser.getUserName(), metaObject);
//        }
//        this.setFieldValByName("updateDate", new Date(), metaObject);
//        this.setFieldValByName("createDate", new Date(), metaObject);
//        if (ObjectUtil.isEmpty(metaObject.getValue("updateBy"))  && currentUser != null) {
//            this.setFieldValByName("updateBy", currentUser.getUserName(), metaObject);
//        }
//        this.setFieldValByName("updateDate", new Date(), metaObject);
//        pinyinSet(metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        CurrentUser currentUser = UserLoginCheckInterceptor.threadLocal.get();
//        if (ObjectUtil.isEmpty(metaObject.getValue("updateBy"))  && currentUser != null) {
//            this.setFieldValByName("updateBy", currentUser.getUserName(), metaObject);
//        }
//        this.setFieldValByName("updateDate", new Date(), metaObject);
//        pinyinSet(metaObject);
//    }
//
//    private void pinyinSet(MetaObject metaObject) {
//        if (metaObject.hasSetter("pinyin") && metaObject.hasGetter("name")) {
//            String pinyin = PinyinUtil.getFirstLetter(String.valueOf(metaObject.getValue("name")), "");
//            this.setFieldValByName("pinyin", pinyin, metaObject);
//        }
//    }

}