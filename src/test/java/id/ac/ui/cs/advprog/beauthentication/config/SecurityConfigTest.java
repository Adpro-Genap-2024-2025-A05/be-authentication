package id.ac.ui.cs.advprog.beauthentication.config;

import id.ac.ui.cs.advprog.beauthentication.filter.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfig.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void publicEndpointsAreAccessibleWithoutAuthentication() throws Exception {
        performGetRequestAndExpectOkStatus("/api");
        performGetRequestAndExpectOkStatus("/api/register/pacilian");
        performGetRequestAndExpectOkStatus("/api/login");
        performGetRequestAndExpectOkStatus("/api/verify");
        performGetRequestAndExpectOkStatus("/error");
    }

    @Test
    void corsIsConfiguredCorrectly() throws Exception {
        mockMvc.perform(options("/api/login")
                .header("Origin", "http://example.com")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Authorization, Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().exists("Access-Control-Allow-Headers"));
    }

    @Test
    void corsConfigurationSourceBeanIsCreated() {
        SecurityConfig securityConfig = new SecurityConfig(jwtAuthFilter, authenticationProvider);
        CorsConfigurationSource corsConfigurationSource = securityConfig.corsConfigurationSource();
        assertNotNull(corsConfigurationSource);
    }

    @Test
    void sessionManagementIsStateless() throws Exception {
        mockMvc.perform(get("/api")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Set-Cookie"));
    }

    @Test
    void csrfIsDisabled() throws Exception {
        performGetRequestAndExpectOkStatus("/api/login");
    }

    @Test
    void logoutReturnsSuccessStatusAndJsonResponse() throws Exception {
        mockMvc.perform(post("/api/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Logged out successfully"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    private void performGetRequestAndExpectOkStatus(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk());
    }
}