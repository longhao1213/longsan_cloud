package com.longsan.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author longhao
 * @since 2021/10/28
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    int insertBatchSomeColumn(List<T> entityList);

    int updateBatch(@Param("list") List<T> list);
}
