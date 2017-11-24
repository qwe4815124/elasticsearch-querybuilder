package com.jing21.elasticsearch.enums;

/**
 * 过滤类型 常量 用常量可以添加我一些没写的
 * Create By zhengjing on 2017/11/23 17:39
 */
public interface FilterType {

    /**
     * 习语匹配 不会分词
     */
    String MATCH_PHRASE = "match_phrase";

    /**
     * 分词查询
     */
    String MATCH = "match";

    /**
     * 精确匹配
     */
    String TERM = "term";

    /**
     * 通配符匹配  eg. 43* 4309* *2233*
     */
    String WILDCARD = "wildcard";

    /**
     * 正则匹配  eg.W[0-9].+
     */
    String REGEXP = "regexp";

    /**
     * 精确匹配多个值
     */
    String TERMS = "terms";

    /**
     * 范围 需要和 FieldValueStrategy 配合使用
     * eg.
     *
     * @ESField=value = "id",query = FilterType.RANGE,valuePolicy = FieldValuePolicy.RANGE_LTE)
     */
    String RANGE = "range";

}
