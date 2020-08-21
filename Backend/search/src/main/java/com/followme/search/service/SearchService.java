package com.followme.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.domain.Field;
import com.followme.search.domain.Multi_match;
import com.followme.search.domain.Query;
import com.followme.search.web.dto.SearchResponseDto;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Service
public class SearchService {
    public JsonNode SearchDoc(int from,String target) throws IOException {
        final SearchResponseDto searchResponseDto;
        RestClient restClient = RestClient.builder(new HttpHost("3.15.22.4",9200,"http")).build();

        Map<String,Object> result = new HashMap<>();

        Field field = new Field(target);
        Multi_match multi_match = new Multi_match(field);
        Query query= new Query(from,5,multi_match);

        String JsonData;

        Gson gson = new Gson();
        JsonData = gson.toJson(query);

        final HttpEntity httpEntity = new NStringEntity(JsonData, ContentType.APPLICATION_JSON);


        Request request = new Request("POST","/x_test/_search");
        request.addParameter("pretty","true");
        request.setEntity(httpEntity);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        JsonNode document = mapper.readTree(responseBody);
        JsonNode document2;
        document2=document.get("hits").get("hits");

        return document2;
    }

}
