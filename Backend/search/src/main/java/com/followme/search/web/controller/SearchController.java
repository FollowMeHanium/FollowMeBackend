package com.followme.search.web.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.followme.search.service.SearchService;
import com.followme.search.web.dto.SearchRequestDto;
import com.followme.search.web.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@RequiredArgsConstructor
@RestController
@Component
public class SearchController {
    private final SearchService searchService;

    @Value("${secretKey}")
    String secret;

    @PostMapping("/search")
    public @ResponseBody List<SearchResponseDto> search(@RequestHeader(value = "token") String token,
                                                        @RequestBody SearchRequestDto searchRequestDto) throws IOException {

        Algorithm a = Algorithm.HMAC256(secret);
        String jwt = JWT.create().withClaim("name","gogo").withClaim("age","30").sign(a);
        System.out.println(secret);
        JWTVerifier verifier = JWT.require(a)
                .build();
        try{
            DecodedJWT decodedJWT = verifier.verify(jwt);
        }catch (JWTVerificationException exception){

        }

        DecodedJWT decodedJWT = JWT.decode(jwt);
        String ab = decodedJWT.getClaim("name").asString();
        Claim age = decodedJWT.getClaim("age");


        return searchService.SearchDoc(searchRequestDto.getFrom(),searchRequestDto.getQuery());
    }


}
