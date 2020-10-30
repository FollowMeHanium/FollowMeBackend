package com.followme.search.domain;

public class Query {
    Object bool;
    public Query(Object multi_match){
        this.bool=multi_match;
    }
}
