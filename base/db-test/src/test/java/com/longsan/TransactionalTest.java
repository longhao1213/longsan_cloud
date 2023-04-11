package com.longsan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.longsan.dao.mapper.SysUserMapper;
import com.longsan.domain.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest(classes = DbApplication.class)
@RequiredArgsConstructor
public class TransactionalTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;


    /**
     * 测试手动开启提交事物，并且一个方法中进行两次
     *
     * 不能提交两次事物
     */
    @Test
    @Transactional
    public void test2() {
        // 手动开启事物
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        try {
            SysUser sysUser = new SysUser();
            sysUser.setUserName("lisi");
            sysUser.setNickName("si");
            sysUserMapper.insert(sysUser);
            // 提交事物
            dataSourceTransactionManager.commit(transaction);
        } catch (Exception e) {
            // 关闭事物
            dataSourceTransactionManager.rollback(transaction);
        }


        transction2(transaction);
    }

    @Transactional
    private void transction2(TransactionStatus transaction) {
        // 第二次获取事物
//        DefaultTransactionDefinition defaultTransactionDefinition2 = new DefaultTransactionDefinition();
//        defaultTransactionDefinition2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        TransactionStatus transaction2 = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition2);
        try {
            SysUser sysUser = new SysUser();
            sysUser.setUserName("lisi2");
            sysUser.setNickName("si2");
            sysUserMapper.insert(sysUser);
            // 提交事物
//            dataSourceTransactionManager.commit(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            // 关闭事物
//            dataSourceTransactionManager.rollback(transaction);
        }
    }


    /**
     * 测试事物传递
     * 方法调用中 a调用b  a和b都有独立的事物，那么a和b的事物会融合在一起，然后按照顺序提交
     */
    @Test
    public void test1() {
        save1();
        SysUser sysUser = select1();
        update1(sysUser);
        sysUser.setNickName("test1");
        sysUserMapper.updateById(sysUser);
    }

    @Transactional
    public void save1() {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("zhangsan");
        sysUser.setNickName("san");
        sysUserMapper.insert(sysUser);
    }

    public SysUser select1() {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, "zhangsan"));
    }

    @Transactional
    public void update1(SysUser sysUser) {
        sysUser.setNickName("update");
        sysUserMapper.updateById(sysUser);
    }


}
