package com.intuit.craftdemo.filter;

import com.intuit.craftdemo.entities.Token;
import com.intuit.craftdemo.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class JwtRequestFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private String createExpiredToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2RoYS5odWthbUBnbWFpbC5jb20iLCJpYXQiOjE3MDI4MzEzMjYsImV4cCI6MTcwMjg2NzMyNn0.mvFlCukI0eUqLx6Kr9wnZU-6_eEOAYsF9oqJnWliBEJkmexqE3LOvAkZ-B094pAwruJ5ixPxgHQmNoEiaTZiVw";
    }

    @Test
    void doFilterInternal_NoToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtRequestFilter.doFilterInternal(request, response, chain);
        verify(request, times(1)).getHeader("Authorization");
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(tokenRepository, never()).findByToken(anyString());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_ValidToken_SuccessfulAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        String validToken = generateValidToken();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(mock(UserDetails.class));
        when(tokenRepository.findByToken(validToken)).thenReturn(java.util.Optional.of(mock(Token.class)));
        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_InvalidToken_NoAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Invalid token (no 'Bearer ')
        when(request.getHeader("Authorization")).thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2RoYS5odWthbUBnbWFpbC5jb20iLCJpYXQiOjE3MDI4MzEzMjYsImV4cCI6MTcwMjg2NzMyNn0.mvFlCukI0eUqLx6Kr9wnZU-6_eEOAYsF9oqJnWliBEJkmexqE3LOvAkZ-B094pAwruJ5fddfdfaTZiVw");

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    public void doFilterInternal_TokenExpired_NoAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String expiredToken = generateExpiredToken();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(tokenRepository.findByToken(expiredToken)).thenReturn(java.util.Optional.of(mock(Token.class)));

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    public void doFilterInternal_InvalidUsername_NoAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String validToken = generateValidToken();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(userDetailsService.loadUserByUsername(any())).thenThrow(new UsernameNotFoundException("User not found"));
        when(tokenRepository.findByToken(validToken)).thenReturn(java.util.Optional.of(mock(Token.class)));

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    // Add more test cases as needed

    private String generateValidToken() {
        // Implement logic to generate a valid token for testing
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2RoYS5odWthbUBnbWFpbC5jb20iLCJpYXQiOjE3MDI4MzEzMjYsImV4cCI6MTcwMjg2NzMyNn0.mvFlCukI0eUqLx6Kr9wnZU-6_eEOAYsF9oqJnWliBEJkmexqE3LOvAkZ-B094pAwruJ5ixPxgHQmNoEiaTZiVw";
    }

    private String generateExpiredToken() {
        // Implement logic to generate an expired token for testing
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2RoYS5odWthbUBnbWFpbC5jb20iLCJpYXQiOjE3MDI4MzEzMjYsImV4cCI6MTcwMjg2NzMyNn0.mvFlCukI0eUqLx6Kr9wnZU-6_eEOAYsF9oqJnWliBEJkmexqE3LOvAkZ-B094pAwruJ5ixPxgHQmNoEiaTZiVw";
    }
}

