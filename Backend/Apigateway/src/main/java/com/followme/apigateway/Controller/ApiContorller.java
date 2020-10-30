package com.followme.apigateway.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiContorller implements ErrorController {
    @Value("${error.path:/error}")
    private String errorPath;


    @Override
    public String getErrorPath() {
        return errorPath;
    }
}
