package com.followme.search.domain;

import lombok.Builder;

public class Query {
    private int from;
    private int size;
    private String[] _source;
    Object query;

    public Query(Object query){
        this.query=query;
    }

    public Query(int from, int size,Object query){
        this.from = from;
        this.size = size;
        this._source = new String[] {"menu","shopname","address","introduce"};
        this.query = query;
    }
}
