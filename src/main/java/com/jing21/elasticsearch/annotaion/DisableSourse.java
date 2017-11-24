package com.jing21.elasticsearch.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用自定义sourse
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/26 16:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableSourse {
}
