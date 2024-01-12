package com.intuit.craftdemo.config;


import com.intuit.craftdemo.filter.JwtRequestFilter;
import com.intuit.craftdemo.service.impl.DriverDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverDetailsService driverDetailsService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private LogoutHandler logoutHandler;


    @Test
    @WithMockUser
    public void testLogoutEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/driver/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

