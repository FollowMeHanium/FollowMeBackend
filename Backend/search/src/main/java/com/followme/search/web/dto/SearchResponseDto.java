package com.followme.search.web.dto;

import com.followme.search.domain.Search;

public class SearchResponseDto {

    private int category;
    private float grade_avg;
    private String menu;
    private String shopname;
    private int likenum;
    private String address;


    public SearchResponseDto(Search entity){
        this.category = entity.getCategory();
        this.grade_avg = entity.getGrade_avg();
        this.shopname = entity.getShopname();
        this.likenum = entity.getLikenum();
        this.address = entity.getAddress();
    }

}
