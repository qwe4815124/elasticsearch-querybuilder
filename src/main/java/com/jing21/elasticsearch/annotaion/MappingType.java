package com.jing21.elasticsearch.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置的类型
 * Create By zhengjing on 2017/11/24 09:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingType {

    /**
     * 配置在查询上 返回值的class 不配置默认返回当前对象
     * @return
     */
    Class<?> value();
}
