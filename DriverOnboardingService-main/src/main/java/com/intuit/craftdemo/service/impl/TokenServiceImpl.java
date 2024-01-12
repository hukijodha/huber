package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.enums.TokenType;
import com.intuit.craftdemo.dto.jwt.JwtResponse;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.entities.Token;
import com.intuit.craftdemo.exception.DriverNotFoundException;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.repository.TokenRepository;
import com.intuit.craftdemo.service.ITokenService;
import com.intuit.craftdemo.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements ITokenService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public JwtResponse generateToken(String email, String password)  {
        Driver driver =driverRepository.findByEmail(email);
        if(driver==null)
        {
            throw new DriverNotFoundException("No driver found with the email Address");
        }
        authenticate(email, password);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);
        revokeAllExistingTokens(driver.getId());
        saveToken(jwtToken, userDetails.getUsername(), driver);
      return  new JwtResponse(jwtToken);
    }

    private void revokeAllExistingTokens(Long driverId)
    {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByDriver(driverId);
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t-> {
          t.setExpired(true);
          t.setRevoked(true);
          });
       tokenRepository.saveAll(validUserTokens);
    }

    private void saveToken(String jwtToken, String email, Driver driver)
    {
        Token token = Token.builder()
                .driver(driver)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .token(jwtToken)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void authenticate(String email, String password)  {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
