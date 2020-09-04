package com.followme.search.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.followme.search.service.SearchService;
import com.followme.search.web.dto.SearchRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@RestController
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/search")
    public JsonNode search(@RequestBody SearchRequestDto searchRequestDto) throws IOException {
        return searchService.SearchDoc(searchRequestDto.getFrom(),searchRequestDto.getQuery());
    }


}
