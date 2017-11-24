package com.jing21.elasticsearch;

import com.jing21.elasticsearch.annotaion.ESField;
import com.jing21.elasticsearch.enums.FilterType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 查询字段配置
 *
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/23 17:35
 */
@Data
@Slf4j
public class EsQueryFieldConfig {

    private static final String DEFAULT_QUERY = "term";

    /**
     * 字段名（java）
     */
    private String fieldName;

    /**
     * Elastic Search 字段名
     */
    private String esFieldName;
    /**
     * 是否参与排序
     */
    private boolean sort = false;

    /**
     * 排序方式
     */
    private String sortType;

    /**
     * 是否显示
     */
    private boolean show = true;

    /**
     * 查询的子查询组
     */
    private String group;

    private String queryType = DEFAULT_QUERY;

    private FieldValueStrategy valuePolicy;

    private FilterType filterType;

    /**
     * 通过注解构建
     *
     * @param fieldName 默认字段名
     * @param esField   字段注解
     * @return
     */
    public static EsQueryFieldConfig buildByAnnotation(String fieldName, ESField esField) {

        EsQueryFieldConfig fieldConfig = new EsQueryFieldConfig();

        fieldConfig.setFieldName(fieldName);
        if (esField == null) {//注解为空使用默认
            fieldConfig.setEsFieldName(fieldName);
            return fieldConfig;
        }

        fieldConfig.setEsFieldName(StringUtils.isNotBlank(esField.value()) ? esField.value() : fieldName);
        fieldConfig.setShow(esField.show());
        fieldConfig.setGroup(esField.group());

        if (Void.class.equals(esField.valuePolicyClass())) {
            fieldConfig.setValuePolicy(esField.valuePolicy());
        } else {
            try {
                Object o = esField.valuePolicyClass().getDeclaredConstructor().newInstance();
                if (o instanceof FieldValueStrategy) {
                    fieldConfig.setValuePolicy((FieldValueStrategy) o);
                }
            } catch (Exception e) {
                throw new IllegalStateException("valuePolicyClass 没有无参的构造函数！");
            }

        }

        if (StringUtils.isNotBlank(esField.sort())) {
            fieldConfig.setSort(true);
            fieldConfig.setSortType(esField.sort());

        }
        fieldConfig.setQueryType(esField.filterType());
        return fieldConfig;
    }

}
