package com.followme.search.domain;

import lombok.Builder;

public class FullQuery {
    private int from;
    private int size;
    private String[] _source;
    private Object query;



    public FullQuery(Object bool){
        this.from = 0;
        this.size = size;
        this._source = new String[] {"menu","shopname","address","introduce","_score","category","main_photo","likenum","grade_avg","photo1","photo2","photo3"
                ,"photo4","photo5","photo6","photo7","photo8","photo9","photo10"};
        this.query=new Query(bool);
    }

    public FullQuery(int from, int size,Object bool){
        this.from = from;
        this.size = size;
        this._source = new String[] {"menu","shopname","address","introduce","_score","category","main_photo","likenum","grade_avg","photo1","photo2","photo3"
                ,"photo4","photo5","photo6","photo7","photo8","photo9","photo10"};
        this.query=new Query(bool);
    }
}
