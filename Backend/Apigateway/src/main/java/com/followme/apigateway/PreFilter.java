package com.followme.apigateway;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
class Validate{
    boolean validated;
    int message;
}

@AllArgsConstructor
class ReturnMessage{
    int code;
    String message;
}

public class PreFilter extends ZuulFilter {
    Logger log = LoggerFactory.getLogger(getClass());
    @Value("${secretKey}")
    String secret;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String authorizationHeader = request.getHeader("token");
        Validate check = validateToken(authorizationHeader);
        Gson gson = new Gson();
        String message;
        if(check.validated==true){
            log.info("validate");
            return null;
        }else{
            log.info("not valide");
            if(check.message==1){
                message="토큰이 변조되었습니다.";
                String Jsondata = gson.toJson(new ReturnMessage(400,message));
                ctx.setResponseStatusCode(400);
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.setResponseBody(Jsondata);
            }else {
                message="토큰이 만되었습니다.";
                String Jsondata = gson.toJson(new ReturnMessage(401,message));
                ctx.setResponseStatusCode(401);
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.setResponseBody(Jsondata);
            }

            return null;
        }
    }

    private Validate validateToken(String tokenHeader) {
        Algorithm a = Algorithm.HMAC256("aaa");
        JWTVerifier verifier = JWT.require(a).build();
        final RequestContext ctx = RequestContext.getCurrentContext();
        try{
            DecodedJWT decodedJWT = verifier.verify(tokenHeader);
            log.info("token validate");
        }catch (SignatureVerificationException exception){
            return new Validate(false,1);
        }catch (TokenExpiredException exception){
            return new Validate(false,2);
        }

        return new Validate(true,0);
    }
}
