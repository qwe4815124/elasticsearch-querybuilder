package com.jing21.elasticsearch.annotaion;

import com.jing21.elasticsearch.FieldValueStrategy;
import com.jing21.elasticsearch.enums.FieldValuePolicy;
import com.jing21.elasticsearch.enums.FilterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Elastic 的字段
 *
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/23 18:00
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESField {

    /**
     * 字段名 默认使用实体字段名
     */
    String value() default "";

    /**
     * 过滤类型
     * @return
     */
    String filterType() default FilterType.MATCH;

    /**
     * 排序类型 为空时不参与排序
     */
    String sort() default "";

    /**
     * 是否显示（是否需要返回）
     */
    boolean show() default true;

    /**
     * 查询时生效
     */
    FieldValuePolicy valuePolicy() default FieldValuePolicy.SIMPLE;

    /**
     * 默认都是filter (TODO 使用filter.must filter.should filter.must_not 等在filter下再生成一个组合条件 )
     */
    String group() default "filter";

    /**
     * 当默认的查询值满足不了你了  那就用这个吧 传一个FieldValueStrategy 的实现类
     * @see FieldValueStrategy
     * @return
     */
    Class<?> valuePolicyClass() default Void.class;
}
