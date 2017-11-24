package com.jing21.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jing21.elasticsearch.annotaion.DisableSourse;
import com.jing21.elasticsearch.annotaion.ESField;
import com.jing21.elasticsearch.annotaion.MappingType;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 构造es查询json
 *
 * @author zhengjing
 * @version 1.0
 * @date: 2017/10/20 14:40
 */
public class QueryBuilder {


    private static Map<String, EsQueryConfig> configs = new HashMap<>();

    private List sorts = new ArrayList();

    private List<String> sources = new ArrayList<>();

    private Map<String, Object> bool = new HashMap<>();

    private EsQueryConfig customConfig = null;

    private Object query;

    /**
     * 查询对象
     *
     * @param query
     */
    public QueryBuilder(Object query) {
        this.query = query;
    }

    /**
     * 自定义配置
     */
    public QueryBuilder config(EsQueryConfig customConfig) {

        this.customConfig = customConfig;
        return this;
    }

    /**
     * 设置静态配置 请在config 方法之后 如果用了的话
     */
    public QueryBuilder staticFiledConfig(EsQueryFieldConfig config) {

        return staticFiledConfig(config.getFieldName(), config);
    }

    /**
     * 设置静态配置 请在config 方法之后 如果用了的话
     */
    public QueryBuilder staticFiledConfig(String fieldName, EsQueryFieldConfig config) {

        if (this.customConfig == null) {
            this.customConfig = new EsQueryConfig();
        }
        customConfig.getStaticConfig().put(fieldName, config);

        return this;
    }

    /**
     * 设置查询配置
     */
    public QueryBuilder queryFieldConfig(String fieldName, EsQueryFieldConfig config) {
        if (this.customConfig == null) {
            this.customConfig = new EsQueryConfig();
        }
        customConfig.getFieldConfigMap().put(fieldName, config);
        return this;
    }

    /**
     * 设置查询配置
     */
    public QueryBuilder queryFieldConfig(EsQueryFieldConfig config) {
        return queryFieldConfig(config.getFieldName(), config);
    }

    /**
     * 构造查询字符串
     *
     * @return
     */
    public String build() {

        EsQueryConfig defaultConfig = parseDefaultConfig(query.getClass());

        JSONObject indexMap = (JSONObject) JSON.toJSON(query);
        //构造字段值
        defaultConfig.getFieldConfigMap().forEach((key, value) -> {

            //如果配置不为空并且存在当前值
            if (customConfig != null && customConfig.getFieldConfigMap().containsKey(key)) {
                value = customConfig.getFieldConfigMap().get(key);
            }

            final EsQueryFieldConfig fieldConfig = value;

            buildCond(fieldConfig, indexMap.get(key));

        });

        //构造显示字段 排序字段等信息
        defaultConfig.getStaticConfig().forEach((key, value) -> {
            //如果配置不为空并且存在当前值
            if (customConfig != null && customConfig.getStaticConfig().containsKey(key)) {
                value = customConfig.getStaticConfig().get(key);
            }

            final EsQueryFieldConfig fieldConfig = value;

            buildSort(fieldConfig);
            if ((customConfig != null && customConfig.isSourse()) || defaultConfig.isSourse()) {
                buildSource(fieldConfig);
            }

        });

        Map<String, Object> json = new HashMap<>();
        if (!sorts.isEmpty())
            json.put("sort", sorts);

        if (!sources.isEmpty())
            json.put("_source", sources);

        Map<String, Object> query = new HashMap<>();
        bool.forEach((key, value) -> {
            List conds = (List) value;
            if (conds.size() == 1) {
                bool.put(key, conds.get(0));
            }
        });
        json.put("query", query);
        query.put("bool", bool);

        return JSON.toJSONString(json);
    }

    /**
     * 解析默认配置
     *
     * @param clazz indexCLass
     * @return
     */
    private static synchronized EsQueryConfig parseDefaultConfig(Class<?> clazz) {
        if (!configs.containsKey(clazz.getSimpleName())) {
            EsQueryConfig queryConfig = new EsQueryConfig();

            queryConfig.setFieldConfigMap(readFieldConfig(clazz));

            /**
             * 配置返回代码
             */
            if (clazz.isAnnotationPresent(MappingType.class)) {
                queryConfig.setMappingType(clazz.getAnnotation(MappingType.class).value());
            } else {
                queryConfig.setMappingType(clazz);
            }

            /**
             * 是否指定显示字段
             */
            if (queryConfig.getMappingType().getAnnotation(DisableSourse.class) != null) {
                queryConfig.setSourse(false);
            }
            /**
             * 获取排序 显示字段 等配置
             */
            if (clazz.equals(queryConfig.getMappingType())) {
                queryConfig.setStaticConfig(queryConfig.getFieldConfigMap());
            } else {
                queryConfig.setStaticConfig(readFieldConfig(queryConfig.getMappingType()));
            }
            configs.put(clazz.getSimpleName(), queryConfig);
        }

        return configs.get(clazz.getSimpleName());
    }

    /**
     * 读取配置
     *
     * @param classic
     * @return
     */
    private static Map<String, EsQueryFieldConfig> readFieldConfig(Class<?> classic) {

        Map<String, EsQueryFieldConfig> fieldConfigs = Stream.of(classic
                .getDeclaredFields())
                //静态变量排除
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                //转换成查询配置
                .map(field -> EsQueryFieldConfig.buildByAnnotation(field.getName(),
                        field.getAnnotation(ESField.class)))
                .collect(Collectors.toMap(EsQueryFieldConfig::getFieldName, Function
                        .identity()));
        return fieldConfigs;

    }

    /**
     * 构造排序
     */
    private void buildSort(EsQueryFieldConfig config) {
        if (config.isSort()) {
            sorts.add(Collections.singletonMap(config.getFieldName(),
                    Collections.singletonMap("order", config.getSortType())));

        }
    }

    /**
     * 构造显示字段
     */
    private void buildSource(EsQueryFieldConfig config) {
        if (config.isShow()) {
            sources.add(config.getEsFieldName());
        }
    }

    /**
     * 构造子查询
     */
    private void buildCond(EsQueryFieldConfig config, Object value) {

        if (Objects.nonNull(value)) {

            List<Map<String, Object>> group = findGroup(config.getGroup());
            Map<String, Object> map = new HashMap<>();
            map.put(config.getQueryType(), Collections.singletonMap(config.getEsFieldName(), config
                    .getValuePolicy().translateValue(value)));
            group.add(map);
        }
    }

    /**
     * 获取子查询组
     *
     * @param group
     */
    private List<Map<String, Object>> findGroup(String group) {
        if (!bool.containsKey(group)) {
            bool.put(group, new ArrayList<>());
        }
        return (List<Map<String, Object>>) bool.get(group);
    }

}
