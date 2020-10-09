package com.followme.search.domain;

public class BoolQuery {
    Object should;
    Object filter;
    public BoolQuery(Object should, Object filter){
        this.should=should;
        this.filter=filter;
    }
}
