package com.jing21.elasticsearch.enums;

import com.jing21.elasticsearch.FieldValueStrategy;

import java.util.*;

/**
 * 处理实际的值
 *
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/25 14:49
 */
public enum FieldValuePolicy implements FieldValueStrategy{
    /**
     * 简单返回
     */
    SIMPLE() {
        public Object translateValue(Object value) {
            return value;
        }
    },

    /**
     * 在本身的值后面添加一个*号  通配符匹配
     */
    START_WITH() {
        public Object translateValue(Object value) {
            return value + "*";
        }
    },
    /**
     * 范围查询  >arg[0] <arg[1]
     */
    RANGE() {
        public Object translateValue(Object value) {
            return ranges(value, "gt", "lt");
        }
    },
    /**
     * 范围查询  >=arg[0] <=arg[1]
     */
    RANGEEE() {
        public Object translateValue(Object value) {
            return ranges(value, "gte", "lte");
        }
    },
    /**
     * 范围查询  >=arg[0] <arg[1]
     */
    RANGEE() {
        public Object translateValue(Object value) {
            return ranges(value, "gte", "lt");
        }
    },
    /**
     * 范围查询  >arg[0] <=arg[1]
     */
    RANGENE() {
        public Object translateValue(Object value) {
            return ranges(value, "gt", "lt");
        }
    },
    /**
     * 小于 生成json
     */
    RANGE_LTE() {
        public Object translateValue(Object value) {
            return Collections.singletonMap("lte", value);
        }
    };

    /**
     * 范围值处理
     *
     * @param value
     * @param gt    大于 gt gte
     * @param lt    小于 lt lte
     * @return 值
     */
    private static Object ranges(Object value, String gt, String lt) {
        if (value instanceof Collection) {
            value = ((Collection) value).toArray();
        }

        if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            if (arr.length == 2) {
                Map<String, Object> map = new HashMap<>(2);
                if (Objects.nonNull(arr[0])) {
                    map.put(gt, arr[0]);
                }
                if (Objects.nonNull(arr[1])) {
                    map.put(lt, arr[1]);
                }
                return map;
            } else if (arr.length == 1) {
                return Collections.singletonMap(gt, value);
            } else {
                throw new IllegalArgumentException("args error");
            }
        }
        return Collections.singletonMap(gt, value);
    }

}
