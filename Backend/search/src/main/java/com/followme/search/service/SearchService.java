package com.followme.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.config.ElasticsearchConf;
import com.followme.search.domain.*;
import com.followme.search.web.dto.SearchRequestDto;
import com.followme.search.web.dto.SearchResponseDto;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Logs{
    int age;
    int gender;
    String searched_at;
    String word;

    Logs(int age,int gender,String word,String searched_at){
        if(age<10){
            this.age=0;
        }else if(age>=10&&age<20){
            this.age=10;
        }else if(age>=20&&age<30){
            this.age=20;
        }else if(age>=30&&age<40){
            this.age=30;
        }else if(age>=40&&age<50){
            this.age=40;
        }else if(age>=50&&age<60){
            this.age=50;
        }else{
            this.age=60;
        }
        this.gender=gender;
        this.word=word;
        this.searched_at=searched_at;
    }
}

@Service
@RequiredArgsConstructor
public class SearchService {
    final private ElasticsearchConf elasticsearchConf;


    //Tear사이에는 POJO만 주고 받도록 리팩토링
    @Transactional(readOnly = true)
    public List<SearchResponseDto> SearchDoc(SearchRequestDto searchRequestDto,int gender,int age) throws IOException {
        List<SearchResponseDto> reList = new ArrayList<>();

        int from=searchRequestDto.getFrom();
        String target=searchRequestDto.getQuery();

        Multi_match multiMatch = new Multi_match(target);
        Should should = new Should(multiMatch);
        Term term = new Term();
        Filter filter = new Filter(term);
        BoolQuery bool = new BoolQuery(should,filter);
        FullQuery fullQuery= new FullQuery(from,20,bool);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date time = new Date();
        String JsonData;
        String Logs;

        Gson gson = new Gson();
        JsonData = gson.toJson(fullQuery); //Object를 Json화
        Logs = gson.toJson(new Logs(age,gender,target,format.format(time)));//log를 위한 json
        final HttpEntity httpEntity = new NStringEntity(JsonData, ContentType.APPLICATION_JSON);
        final HttpEntity httpLogs = new NStringEntity(Logs,ContentType.APPLICATION_JSON);


        //String responseBody = EntityUtils.toString(response.getEntity());
        if(!(age==-1)&&!(gender==-1)){
            Request requestLog = new Request("POST","/search_log_datas/_doc");//ES에 로그 저장
            requestLog.addParameter("pretty","true");
            requestLog.setEntity(httpLogs);
            Response response1 = elasticsearchConf.getRestClient().performRequest(requestLog);
        }

        Request request = new Request("POST","/infos_table/_search");
        request.addParameter("pretty","true");
        request.setEntity(httpEntity);
        Response response = elasticsearchConf.getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        JsonNode document = mapper.readTree(responseBody);
        JsonNode document2;
        document2=document.get("hits").get("hits");

        for(int i=0;i<document2.size();i++){
            JsonNode mainPhoto = document2.get(i).get("_source").get("main_photo");
            String photo = "photo"+mainPhoto.toString();
            if(photo.equals("photonull")){
                reList.add(SearchResponseDto.builder()
                        .id(document2.get(i).get("_id").asInt())
                        .score(document2.get(i).get("_score").floatValue())
                        .shopname(document2.get(i).get("_source").get("shopname").asText())
                        .address(document2.get(i).get("_source").get("address").asText())
                        .category(document2.get(i).get("_source").get("category").asInt())
                        .grade_avg(document2.get(i).get("_source").get("grade_avg").floatValue())
                        .menu(document2.get(i).get("_source").get("menu").asText())
                        .likenum(document2.get(i).get("_source").get("likenum").asInt())
                        .photo("No Photo")
                        .build());
            }else{
                reList.add(SearchResponseDto.builder()
                        .id(document2.get(i).get("_id").asInt())
                        .score(document2.get(i).get("_score").floatValue())
                        .shopname(document2.get(i).get("_source").get("shopname").asText())
                        .address(document2.get(i).get("_source").get("address").asText())
                        .category(document2.get(i).get("_source").get("category").asInt())
                        .grade_avg(document2.get(i).get("_source").get("grade_avg").floatValue())
                        .menu(document2.get(i).get("_source").get("menu").asText())
                        .likenum(document2.get(i).get("_source").get("likenum").asInt())
                        .photo(document2.get(i).get("_source").get(photo).asText())
                        .build());//builder 패턴
            }
        }
        return reList.stream().collect(Collectors.toList());
    }



}
