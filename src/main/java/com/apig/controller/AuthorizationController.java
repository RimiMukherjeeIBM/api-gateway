package com.apig.controller;

import com.apig.bean.UserAuthCredentials;
import com.apig.bean.auth.JwtToken;
import com.apig.model.AuthTokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    @Autowired
    JwtToken jwtToken;

    @PostMapping("/authorize")
    public AuthTokenResponse authorize(@RequestHeader(value = "X-IBM-Client-Id") String clientId,
                                       @RequestHeader(value = "X-IBM-Client-Secret") String clientSecret){
        return jwtToken.buildJwtToken(clientId, clientSecret);
    }
}
