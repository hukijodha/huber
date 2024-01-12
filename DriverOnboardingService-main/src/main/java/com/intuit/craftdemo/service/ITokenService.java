package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.jwt.JwtResponse;

public interface ITokenService {
    JwtResponse generateToken(String email, String password) throws Exception;
}
