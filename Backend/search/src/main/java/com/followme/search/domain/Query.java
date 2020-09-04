package com.followme.search.domain;

public class Query {
    Object query_string;
    Query(Object query_string){
        this.query_string=query_string;
    }
}
