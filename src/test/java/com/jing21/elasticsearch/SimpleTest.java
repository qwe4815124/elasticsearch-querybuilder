package com.jing21.elasticsearch;

import org.junit.Test;

/**
 * Create By zhengjing on 2017/11/24 11:03
 */
public class SimpleTest {

    @Test
    public void simpleTest(){

        ArticleQuery query = new ArticleQuery();
        query.setContent("内容");

        query.setLastid(12L);
        query.setTitle("标题");
        QueryBuilder queryBuilder = new QueryBuilder(query);
        System.err.println(queryBuilder.build());
    }
}
