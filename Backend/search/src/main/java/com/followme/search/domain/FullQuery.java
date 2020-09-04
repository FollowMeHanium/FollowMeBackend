package com.followme.search.domain;

import lombok.Builder;

public class FullQuery {
    private int from;
    private int size;
    private String[] _source;
    private Object query;



    public FullQuery(Object query_string){
        this.from = 0;
        this.size = size;
        this._source = new String[] {"menu","shopname","address","introduce","_score"};
        this.query=new Query(query_string);
    }

    public FullQuery(int from, int size,Object query_string){
        this.from = from;
        this.size = size;
        this._source = new String[] {"menu","shopname","address","introduce","_score"};
        this.query=new Query(query_string);
    }
}
