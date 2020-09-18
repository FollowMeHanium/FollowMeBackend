package com.followme.search.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;


@Setter
@Getter
public class QueryString {
    private Object query;
    private String[] fields;
    private Object exists;

    public QueryString(String query){
        this.query = query+" AND is_deleted:false";//삭제된 가게 정보를 제외한 검색
        this.fields= new String[]{"menu", "shopname", "introduce", "address"};
    }
}
