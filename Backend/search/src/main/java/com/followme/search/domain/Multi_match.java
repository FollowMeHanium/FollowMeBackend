package com.followme.search.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;


@Setter
@Getter
public class Multi_match {
    private Object query;
    private String[] fields;

    public Multi_match(String query){
        this.query = query;
        this.fields= new String[]{"menu", "shopname", "introduce", "address"};
    }
}
