package com.followme.search.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
public class ElasticsearchConf implements InitializingBean {
    @Value("${elasticsearch.host}")
    String hostIP;
    @Value("${elasticsearch.port}")
    String hostPort;
    private RestClient restClient;

    public RestClient getRestClient(){
        return restClient;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        this.restClient=RestClient.builder(new HttpHost(hostIP, Integer.parseInt(hostPort),"http")).build();
    }
}

