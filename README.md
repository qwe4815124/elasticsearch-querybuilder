# elasticsearch-querybuilder
elasticsearch 查询json串生成

也没有找到几个好用的es库 于是就自己写了一个 根据实体生成es查询json

```java
@DisableSourse
public class ArticleQuery{

    @ESField(sort = "desc")
    private Long id;
    
    @ESField
    private Integer state;

    @ESField(filterType = FilterType.TERM,valuePolicyClass = MyValue.class)
    private String type;

    @ESField(value = "id", filterType = FilterType.RANGE, valuePolicy = FieldValuePolicy.RANGE_LTE)
    private Long lastid;

    @ESField(filterType = FilterType.MATCH_PHRASE, group = "should")
    private String title;

    @ESField(group = "must")
    private String content;

}

```

生成后的json
```json
{
    "query": {
        "bool": {
            "filter": [
                {
                    "match": {
                        "state": 1
                    }
                },
                {
                    "term": {
                        "type": "nmmp"
                    }
                }
            ],
            "must": {
                "match": {
                    "content": "content"
                }
            }
        }
    },
    "sort": [
        {
            "id": {
                "order": "desc"
            }
        }
    ]
}
```
 
注解 `@DisableSourse` 配置不会生成_source 不配置会生成

如果你的返回类和查询类不是同一个类需要在查询类上加一个(_sort sourse 是返回类中获取的)
