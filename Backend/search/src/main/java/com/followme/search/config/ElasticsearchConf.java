package com.followme.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConf {
    private RestClient restClient = RestClient.builder(new HttpHost("3.15.22.4",9200,"http")).build();

    public RestClient getRestClient(){
        return restClient;
    }
}

