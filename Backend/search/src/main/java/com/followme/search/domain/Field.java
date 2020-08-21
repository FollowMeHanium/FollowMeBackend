package com.followme.search.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Setter
@Getter
public class Field {
    private Object query;
    private String[] fields;

    public Field(String query){
        this.query = query;
        this.fields= new String[]{"menu", "shopname", "introduce", "address"};
    }
}
