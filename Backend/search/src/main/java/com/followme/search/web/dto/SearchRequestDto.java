package com.followme.search.web.dto;

import com.followme.search.domain.Query;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {
    private int from;
    private int size;
    private String query;

    public SearchRequestDto(int from, String query){
        this.from = from;
        this.size = 5;
        this.query = query;
    }
}
