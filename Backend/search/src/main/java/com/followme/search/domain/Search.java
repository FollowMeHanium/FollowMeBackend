package com.followme.search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class Search {
    private int category;
    private float grade_avg;
    private String shopname;
    private int likenum;
    private String address;

    public Search(int category,float grade_avg,String shopname,int likenum,String address){
        this.shopname = shopname;
        this.address=address;
        this.category=category;
        this.grade_avg=grade_avg;
        this.likenum=likenum;
    }
}
