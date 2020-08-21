package com.followme.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.domain.Field;
import com.followme.search.domain.Multi_match;
import com.followme.search.domain.Query;
import com.followme.search.service.SearchService;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class SearchApplication {
    public static void main(String[] args) throws IOException {
        /*
        SearchService searchService = new SearchService();

        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest firstSearchReq = new SearchRequest();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("menu","곱창전골"));
        firstSearchReq.source(searchSourceBuilder);
        request.add(firstSearchReq);

        SearchRequest secondSearchReq = new SearchRequest();
        searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("shopname","족발"));
        secondSearchReq.source(searchSourceBuilder);
        request.add(secondSearchReq);

        MultiSearchResponse response = client.msearch(request,RequestOptions.DEFAULT);


 */

        SpringApplication.run(SearchApplication.class, args);
    }

}
