package com.jing21.elasticsearch;

/**
 * 值处理策略
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/25 14:45
 */
@FunctionalInterface
public interface FieldValueStrategy {

    Object translateValue(Object value);
}
