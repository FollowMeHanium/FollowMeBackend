package com.followme.search.web.dto;

import com.followme.search.domain.Search;

public class SearchResponseDto {

    private String Id;
    private String category;
    private float grade_avg;
    private String menu;
    private String shopname;
    private int likenum;
    private String address;
    private int reviewnum;
    private String introduce;

    public SearchResponseDto(Search entity){
        this.category = entity.getCategory();
        this.grade_avg = entity.getGrade_avg();
        this.menu = entity.getMenu();
        this.shopname = entity.getShopname();
        this.likenum = entity.getLikenum();
        this.address = entity.getAddress();
        this.reviewnum = entity.getReviewnum();
        this.introduce = entity.getIntroduce();
    }

}
