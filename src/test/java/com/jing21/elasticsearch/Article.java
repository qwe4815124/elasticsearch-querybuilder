package com.jing21.elasticsearch;

import com.jing21.elasticsearch.annotaion.ESField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 朋友圈 对应的elastic 文档类
 *
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/19 16:37
 */
@Data
public class Article {

    @ESField(sort = "desc")
    private Long id;

    private String type;

    private String title;

    private String content;

    private Integer state;

    @ESField(show = false)
    private LocalDateTime addTime;

    private Long readnum;


}
