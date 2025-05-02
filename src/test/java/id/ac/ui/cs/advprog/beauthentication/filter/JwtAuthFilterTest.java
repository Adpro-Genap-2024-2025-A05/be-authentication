package id.ac.ui.cs.advprog.beauthentication.filter;

import id.ac.ui.cs.advprog.beauthentication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternalNoAuthHeaderProceedsWithChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        verifyNoInteractions(securityContext);
    }

    @Test
    void doFilterInternalInvalidAuthHeaderFormatProceedsWithChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        verifyNoInteractions(securityContext);
    }

    @Test
    void doFilterInternalValidTokenSetsAuthentication() throws ServletException, IOException {
        String token = "valid-token";
        String userEmail = "user@example.com";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(userEmail);
        verify(jwtService).isTokenValid(token, userDetails);
        verify(securityContext).getAuthentication();
        verify(securityContext).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalInvalidTokenDoesNotSetAuthentication() throws ServletException, IOException {
        String token = "invalid-token";
        String userEmail = "user@example.com";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(userEmail);
        verify(jwtService).isTokenValid(token, userDetails);
        verify(securityContext).getAuthentication();
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalNullUsernameDoesNotSetAuthentication() throws ServletException, IOException {
        String token = "null-username-token";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verifyNoInteractions(userDetailsService);
        verify(securityContext, never()).getAuthentication();
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalExistingAuthenticationDoesNotSetAuthentication() throws ServletException, IOException {
        String token = "valid-token";
        String userEmail = "user@example.com";
        
        UsernamePasswordAuthenticationToken existingAuth = 
                new UsernamePasswordAuthenticationToken("user", null, null);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(securityContext.getAuthentication()).thenReturn(existingAuth);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verifyNoInteractions(userDetailsService);
        verify(securityContext).getAuthentication();
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalExceptionThrownHandlesException() throws ServletException, IOException {
        String token = "exception-token";
        RuntimeException exception = new RuntimeException("Token error");
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenThrow(exception);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(token);
        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verify(filterChain, never()).doFilter(request, response);
    }
}