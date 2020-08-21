package com.followme.search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {
    private String category;
    private float grade_avg;
    private String menu;
    private String shopname;
    private int likenum;
    private String address;
    private int reviewnum;
    private String introduce;

    public Search(String category,float grade_avg,String menu,String shopname,int likenum,String address,int reviewnum,String introduce){

    }
}
