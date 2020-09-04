package com.followme.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.domain.FullQuery;
import com.followme.search.domain.Query;
import com.followme.search.domain.QueryString;
import com.followme.search.web.dto.SearchResponseDto;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Service
public class SearchService {
    public JsonNode SearchDoc(int from,String target) throws IOException {
        final SearchResponseDto searchResponseDto;
        RestClient restClient = RestClient.builder(new HttpHost("3.15.22.4",9200,"http")).build();

        Map<String,Object> result = new HashMap<>();

        QueryString queryString = new QueryString(target);
        FullQuery fullQuery= new FullQuery(from,5,queryString);

        String JsonData;

        Gson gson = new Gson();
        JsonData = gson.toJson(fullQuery); //Object를 Json화

        final HttpEntity httpEntity = new NStringEntity(JsonData, ContentType.APPLICATION_JSON);


        Request request = new Request("POST","/infos_table/_search");
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
