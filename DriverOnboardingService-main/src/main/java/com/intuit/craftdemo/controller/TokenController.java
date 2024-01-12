package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.jwt.JwtRequest;
import com.intuit.craftdemo.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {



    @Autowired
    private ITokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(tokenService.generateToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
    }


}
