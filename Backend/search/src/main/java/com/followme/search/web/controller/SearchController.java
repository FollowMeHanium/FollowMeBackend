package com.followme.search.web.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followme.search.service.SearchService;
import com.followme.search.web.dto.SearchRequestDto;
import com.followme.search.web.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Component
public class SearchController {
    private final SearchService searchService;
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${secretKey}")
    String secret;

    @PostMapping("/search")
    public @ResponseBody List<SearchResponseDto> search(@RequestHeader(value = "authorization") String token,
                                                        @RequestBody SearchRequestDto searchRequestDto) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Algorithm a = Algorithm.HMAC256(secret);
        String jwt = token;
        JWTVerifier verifier = JWT.require(a)
                .build();
        try{
            DecodedJWT decodedJWT = verifier.verify(jwt);
        }catch (JWTVerificationException exception){

        }

        DecodedJWT decodedJWT = JWT.decode(jwt);
        int gender = decodedJWT.getClaim("gender").asInt();
        int age = decodedJWT.getClaim("age").asInt();
        log.trace("gender-"+gender+" "+ "age-"+age);

        return searchService.SearchDoc(searchRequestDto.getFrom(),searchRequestDto.getQuery());
    }


}
