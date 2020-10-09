package com.followme.search.web.dto;

import com.followme.search.domain.Search;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchResponseDto {
    private int id;
    private int category;
    private float grade_avg;
    private String menu;
    private String shopname;
    private int likenum;
    private String address;
    private String photo;
    private float score;
}
