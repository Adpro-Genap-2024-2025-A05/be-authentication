package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class RegistrationTests {
        @Test
        void registerPacilianValidDataReturnsCreatedStatus() {
            RegisterPacilianDto registerDto = createPacilianDto();
            RegisterResponseDto expectedResponse = createRegisterResponse("patient-id", Role.PACILIAN);
            
            when(authService.registerPacilian(any(RegisterPacilianDto.class))).thenReturn(expectedResponse);
            
            ResponseEntity<RegisterResponseDto> response = authController.registerPacilian(registerDto);
            
            assertRegistrationResponse(response, expectedResponse, HttpStatus.CREATED);
            verify(authService).registerPacilian(registerDto);
        }

        @Test
        void registerCaregiverValidDataReturnsCreatedStatus() {
            RegisterCaregiverDto registerDto = createCaregiverDto();
            RegisterResponseDto expectedResponse = createRegisterResponse("caregiver-id", Role.CAREGIVER);
            
            when(authService.registerCaregiver(any(RegisterCaregiverDto.class))).thenReturn(expectedResponse);
            
            ResponseEntity<RegisterResponseDto> response = authController.registerCaregiver(registerDto);
            
            assertRegistrationResponse(response, expectedResponse, HttpStatus.CREATED);
            verify(authService).registerCaregiver(registerDto);
        }

        private RegisterPacilianDto createPacilianDto() {
            return RegisterPacilianDto.builder()
                    .email("patient@example.com")
                    .password("password")
                    .name("Patient Name")
                    .nik("1234567890123456")
                    .address("Patient Address")
                    .phoneNumber("1234567890")
                    .medicalHistory("Some medical history")
                    .build();
        }

        private RegisterCaregiverDto createCaregiverDto() {
            return RegisterCaregiverDto.builder()
                    .email("caregiver@example.com")
                    .password("password")
                    .name("Caregiver Name")
                    .nik("1234567890123456")
                    .workAddress("Work Address")
                    .phoneNumber("1234567890")
                    .speciality("Cardiology")
                    .workingSchedules(new ArrayList<>())
                    .build();
        }

        private RegisterResponseDto createRegisterResponse(String id, Role role) {
            return RegisterResponseDto.builder()
                    .id(id)
                    .role(role)
                    .message("Registration successful. Please login.")
                    .build();
        }

        private void assertRegistrationResponse(
                ResponseEntity<RegisterResponseDto> response, 
                RegisterResponseDto expectedResponse, 
                HttpStatus expectedStatus) {
            assertEquals(expectedStatus, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
        }
    }

    @Nested
    class AuthenticationTests {
        @Test
        void loginValidCredentialsReturnsOkStatus() {
            LoginDto loginDto = createLoginDto();
            LoginResponseDto expectedResponse = createLoginResponse();
            
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

        private LoginDto createLoginDto() {
            return LoginDto.builder()
                    .email("user@example.com")
                    .password("password")
                    .build();
        }

        private LoginResponseDto createLoginResponse() {
            return LoginResponseDto.builder()
                    .accessToken("jwt-token")
                    .email("user@example.com")
                    .name("User Name")
                    .role(Role.PACILIAN)
                    .expiresIn(3600L)
                    .build();
        }
    }

    @Nested
    class TokenVerificationTests {
        private static final String VALID_TOKEN = "valid-token";
        private static final String INVALID_TOKEN = "invalid-token";
        private static final String AUTH_HEADER = "Authorization";
        private static final String BEARER_PREFIX = "Bearer ";

        @Test
        void verifyTokenValidTokenReturnsOkStatus() {
            TokenVerificationResponseDto expectedResponse = createTokenVerificationResponse(true);
            setupTokenVerification(VALID_TOKEN, expectedResponse);
            
            ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
            
            assertTokenVerificationResponse(response, expectedResponse, HttpStatus.OK);
            verify(authService).verifyToken(VALID_TOKEN);
        }

        @Test
        void verifyTokenInvalidTokenReturnsUnauthorizedStatus() {
            TokenVerificationResponseDto expectedResponse = TokenVerificationResponseDto.builder()
                    .valid(false)
                    .build();
            
            setupTokenVerification(INVALID_TOKEN, expectedResponse);
            
            ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
            
            assertTokenVerificationResponse(response, expectedResponse, HttpStatus.UNAUTHORIZED);
            verify(authService).verifyToken(INVALID_TOKEN);
        }

        @Test
        void verifyTokenNoAuthHeaderReturnsUnauthorizedStatus() {
            when(httpServletRequest.getHeader(AUTH_HEADER)).thenReturn(null);
            
            ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertFalse(response.getBody().isValid());
            verifyNoInteractions(authService);
        }

        @Test
        void verifyTokenInvalidAuthHeaderFormatReturnsUnauthorizedStatus() {
            when(httpServletRequest.getHeader(AUTH_HEADER)).thenReturn("InvalidFormat token");
            
            ResponseEntity<TokenVerificationResponseDto> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertFalse(response.getBody().isValid());
            verifyNoInteractions(authService);
        }

        private TokenVerificationResponseDto createTokenVerificationResponse(boolean isValid) {
            if (!isValid) {
                return TokenVerificationResponseDto.builder().valid(false).build();
            }
            
            return TokenVerificationResponseDto.builder()
                    .valid(true)
                    .userId("user-id")
                    .email("user@example.com")
                    .role(Role.PACILIAN)
                    .expiresIn(3600L)
                    .build();
        }

        private void setupTokenVerification(String token, TokenVerificationResponseDto response) {
            when(httpServletRequest.getHeader(AUTH_HEADER)).thenReturn(BEARER_PREFIX + token);
            when(authService.verifyToken(token)).thenReturn(response);
        }

        private void assertTokenVerificationResponse(
                ResponseEntity<TokenVerificationResponseDto> response,
                TokenVerificationResponseDto expectedResponse,
                HttpStatus expectedStatus) {
            assertEquals(expectedStatus, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
        }
    }
}