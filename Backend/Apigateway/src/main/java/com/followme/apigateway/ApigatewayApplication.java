package com.followme.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
@EnableZuulProxy
public class ApigatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }

    @Bean
    public PreFilter prefilter() {
        return new PreFilter();
    }

    @Bean
    public PostFilter postFilter(){
        return new PostFilter();
    }

    @Bean
    public ErrorFilter errorFilter(){
        return new ErrorFilter();
    }

    @Bean
    public RouteFilter routeFilter(){
        return new RouteFilter();
    }
}
