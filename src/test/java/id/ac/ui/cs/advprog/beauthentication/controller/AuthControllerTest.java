package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void healthCheckReturnsOkStatus() {
        ResponseEntity<Map<String, String>> response = authController.healthCheck();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("Authentication API", response.getBody().get("service"));
    }

    @Test
    void registerPacilianValidDataReturnsCreatedStatus() {
        RegisterPacilianDto registerDto = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();
        
        RegisterResponseDto expectedResponse = RegisterResponseDto.builder()
                .id("patient-id")
                .role(Role.PACILIAN)
                .message("Registration successful. Please login.")
                .build();
        
        when(authService.registerPacilian(any(RegisterPacilianDto.class))).thenReturn(expectedResponse);
        
        ResponseEntity<RegisterResponseDto> response = authController.registerPacilian(registerDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).registerPacilian(registerDto);
    }

    @Test
    void registerCaregiverValidDataReturnsCreatedStatus() {
        RegisterCaregiverDto registerDto = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(new ArrayList<>())
                .build();
        
        RegisterResponseDto expectedResponse = RegisterResponseDto.builder()
                .id("caregiver-id")
                .role(Role.CAREGIVER)
                .message("Registration successful. Please login.")
                .build();
        
        when(authService.registerCaregiver(any(RegisterCaregiverDto.class))).thenReturn(expectedResponse);
        
        ResponseEntity<RegisterResponseDto> response = authController.registerCaregiver(registerDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).registerCaregiver(registerDto);
    }

    @Test
    void loginValidCredentialsReturnsOkStatus() {
        LoginDto loginDto = LoginDto.builder()
                .email("user@example.com")
                .password("password")
                .build();
        
        LoginResponseDto expectedResponse = LoginResponseDto.builder()
                .accessToken("jwt-token")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .expiresIn(3600L)
                .build();
        
        when(authService.login(any(LoginDto.class))).thenReturn(expectedResponse);
        
        ResponseEntity<LoginResponseDto> response = authController.login(loginDto);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).login(loginDto);
    }

    @Test
    void logoutReturnsOkStatus() {
        ResponseEntity<Map<String, String>> response = authController.logout(httpServletRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logged out successfully", response.getBody().get("message"));
        assertEquals("success", response.getBody().get("status"));
        verify(authService).logout();
    }

    @Test
    void verifyTokenValidTokenReturnsOkStatus() {
        String token = "valid-token";
        TokenVerificationResponseDto expectedResponse = TokenVerificationResponseDto.builder()
                .valid(true)
                .userId("user-id")
                .email("user@example.com")
                .role(Role.PACILIAN)
                .expiresIn(3600L)
                .build();
        
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(authService.verifyToken(token)).thenReturn(expectedResponse);
        
        ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).verifyToken(token);
    }

    @Test
    void verifyTokenInvalidTokenReturnsUnauthorizedStatus() {
        String token = "invalid-token";
        TokenVerificationResponseDto expectedResponse = TokenVerificationResponseDto.builder()
                .valid(false)
                .build();
        
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(authService.verifyToken(token)).thenReturn(expectedResponse);
        
        ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).verifyToken(token);
    }

    @Test
    void verifyTokenNoAuthHeaderReturnsUnauthorizedStatus() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        
        ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isValid());
        verifyNoInteractions(authService);
    }

    @Test
    void verifyTokenInvalidAuthHeaderFormatReturnsUnauthorizedStatus() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("InvalidFormat token");
        
        ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isValid());
        verifyNoInteractions(authService);
    }
}