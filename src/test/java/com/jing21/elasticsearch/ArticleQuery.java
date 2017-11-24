package com.jing21.elasticsearch;

import com.jing21.elasticsearch.annotaion.ESField;
import com.jing21.elasticsearch.annotaion.MappingType;
import com.jing21.elasticsearch.enums.FieldValuePolicy;
import com.jing21.elasticsearch.enums.FilterType;
import lombok.Data;

/**
 * 文章查询
 */
@Data
@MappingType(Article.class)
public class ArticleQuery {

    @ESField(filterType = FilterType.TERM)
    private Integer state;

    @ESField(filterType = FilterType.TERM)
    private String type;

    @ESField(value = "id", filterType = FilterType.RANGE, valuePolicy = FieldValuePolicy.RANGE_LTE)
    private Long lastid;

    @ESField(filterType = FilterType.MATCH_PHRASE, group = "should")
    private String title;

    @ESField(filterType = FilterType.MATCH, group = "must")
    private String content;

}
