package com.followme.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.config.ElasticsearchConf;
import com.followme.search.domain.FullQuery;
import com.followme.search.domain.Query;
import com.followme.search.domain.QueryString;
import com.followme.search.domain.Search;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {
    final private ElasticsearchConf elasticsearchConf;
    public JsonNode SearchDoc(int from,String target) throws IOException {
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
        Response response = elasticsearchConf.getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        JsonNode document = mapper.readTree(responseBody);
        JsonNode document2;
        document2=document.get("hits").get("hits");

        elasticsearchConf.getRestClient().close();
/*
        search.setAddress(document2.get("_source").get("address").toString());
        search.setCategory(document2.get("_source").get("catagory").toString());
*/
        return document2;
    }



}
