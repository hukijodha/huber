package com.intuit.craftdemo.dto;

import com.intuit.craftdemo.dto.driver.DriverDetails;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class DriverDetailsTest {

    @Test
    void getUsername_ShouldReturnEmail() {
        String email = "test@example.com";
        DriverDetails driverDetails = new DriverDetails(email, "password", Collections.emptyList());
        String username = driverDetails.getUsername();
        assertEquals(email, username);
    }

    @Test
    void getPassword_ShouldReturnPassword() {
        String password = "secret";
        DriverDetails driverDetails = new DriverDetails("test@example.com", password, Collections.emptyList());
        String actualPassword = driverDetails.getPassword();
        assertEquals(password, actualPassword);
    }

    @Test
    void getAuthorities_ShouldReturnAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        DriverDetails driverDetails = new DriverDetails("test@example.com", "password", Collections.singletonList(authority));
        var authorities = driverDetails.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.contains(authority));
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        DriverDetails driverDetails = new DriverDetails("test@example.com", "password", Collections.emptyList());
        boolean accountNonExpired = driverDetails.isAccountNonExpired();
        assertTrue(accountNonExpired);
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        DriverDetails driverDetails = new DriverDetails("test@example.com", "password", Collections.emptyList());
        boolean accountNonLocked = driverDetails.isAccountNonLocked();
        assertTrue(accountNonLocked);
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        DriverDetails driverDetails = new DriverDetails("test@example.com", "password", Collections.emptyList());
        boolean credentialsNonExpired = driverDetails.isCredentialsNonExpired();
        assertTrue(credentialsNonExpired);
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        DriverDetails driverDetails = new DriverDetails("test@example.com", "password", Collections.emptyList());
        boolean enabled = driverDetails.isEnabled();
        assertTrue(enabled);
    }
}
