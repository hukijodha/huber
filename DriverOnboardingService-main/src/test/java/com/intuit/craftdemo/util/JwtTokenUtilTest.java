package com.intuit.craftdemo.util;
import com.intuit.craftdemo.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenUtilTest {

    private static final String SECRET_KEY = "50D78783DF40639445E739E6792174C4BFDE5EDE014E54DF0E11D5862C12BEC4E739E6792174C4BFE739E6792174C4BFC4E739E6792174C4BFE739E6792174C4BFC4E739E6792174C4BFE739E6792174C4BF"; // Replace with your actual secret key

    @Test
    void generateToken_ShouldGenerateToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        jwtTokenUtil.secretKey = SECRET_KEY;
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtTokenUtil.generateToken(userDetails);
        assertNotNull(token);
    }
}

