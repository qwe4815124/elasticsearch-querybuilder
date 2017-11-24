package com.jing21.elasticsearch;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/23 17:23
 */
@Data
public class EsQueryConfig {

    /**
     * 是否制定返回的字段
     */
    private boolean sourse = true;

    private Class<?> mappingType;

    /**
     * 静态配置  排序 返回字段 等信息
     */
    Map<String, EsQueryFieldConfig> staticConfig = new HashMap<>();

    /**
     * 查询配置 查询类型 value过了等等
     */
    Map<String, EsQueryFieldConfig> fieldConfigMap = new HashMap<>();

}
