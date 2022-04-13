package com.hlju.onlineshop.search;

import com.alibaba.fastjson.JSON;
import com.hlju.onlineshop.search.conf.ElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class OnlineShopSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoad() {
        System.out.println(client);
    }

    @Data
    static class User {
        private String userName;
        private Integer age;
        private String gender;
    }

    /**
     * 测试存储数据到es
     * 保存 / 更新
     */
    @Test
    public void indexTest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setUserName("haoqiang");
        user.setGender("男");
        user.setAge(22);
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @ToString
    @Data
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    /**
     * 测试查询数据
     */
    @Test
    public void searchTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // sourceBuilder.from();
        // sourceBuilder.size();
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        // 按照年龄值分布聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        AvgAggregationBuilder ageBalanceAvg = AggregationBuilders.avg("ageBalanceAvg").field("balance");
        ageAgg.subAggregation(ageBalanceAvg);

        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println(sourceBuilder.toString());

        searchRequest.source(sourceBuilder);

        SearchResponse search = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        // System.out.println(search.toString());
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String string = hit.getSourceAsString();
            Account account = JSON.parseObject(string, Account.class);
            System.out.println("account" + account);
        }
        Aggregations aggregations = search.getAggregations();
        for (Aggregation aggregation : aggregations.asList()) {
            System.out.println("name: " + aggregation.getName());
        }
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            Avg avg = bucket.getAggregations().get("ageBalanceAvg");
            System.out.println("年龄" + bucket.getKeyAsString() + "\t" + bucket.getDocCount() + "人" + "\t" + avg.getValue());
        }
        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资" + balanceAvg1.getValue());
    }
}
