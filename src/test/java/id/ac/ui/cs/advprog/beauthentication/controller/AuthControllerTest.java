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

    @Nested
    class RegistrationTests {
        @Test
        void registerPacilianValidDataReturnsCreatedStatus() {
            RegisterPacilianDto registerDto = createPacilianDto();
            RegisterResponseDto expectedResponse = createRegisterResponse("patient-id", Role.PACILIAN);
            
            when(authService.registerPacilian(any(RegisterPacilianDto.class))).thenReturn(expectedResponse);
            
            ResponseEntity<ApiResponseDto<RegisterResponseDto>> response = authController.registerPacilian(registerDto);
            
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            ApiResponseDto<RegisterResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.CREATED.value(), body.getStatus());
            assertEquals("Pacilian registered successfully", body.getMessage());
            assertNotNull(body.getTimestamp());
            assertEquals(expectedResponse, body.getData());
            
            verify(authService).registerPacilian(registerDto);
        }

        @Test
        void registerCaregiverValidDataReturnsCreatedStatus() {
            RegisterCaregiverDto registerDto = createCaregiverDto();
            RegisterResponseDto expectedResponse = createRegisterResponse("caregiver-id", Role.CAREGIVER);
            
            when(authService.registerCaregiver(any(RegisterCaregiverDto.class))).thenReturn(expectedResponse);
            
            ResponseEntity<ApiResponseDto<RegisterResponseDto>> response = authController.registerCaregiver(registerDto);
            
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            ApiResponseDto<RegisterResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.CREATED.value(), body.getStatus());
            assertEquals("Caregiver registered successfully", body.getMessage());
            assertNotNull(body.getTimestamp());
            assertEquals(expectedResponse, body.getData());
            
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
                    .build();
        }

        private RegisterResponseDto createRegisterResponse(String id, Role role) {
            return RegisterResponseDto.builder()
                    .id(id)
                    .role(role)
                    .message("Registration successful. Please login.")
                    .build();
        }
    }

    @Nested
    class AuthenticationTests {
        @Test
        void loginValidCredentialsReturnsOkStatus() {
            LoginDto loginDto = createLoginDto();
            LoginResponseDto expectedResponse = createLoginResponse();
            
            when(authService.login(any(LoginDto.class))).thenReturn(expectedResponse);
            
            ResponseEntity<ApiResponseDto<LoginResponseDto>> response = authController.login(loginDto);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ApiResponseDto<LoginResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK.value(), body.getStatus());
            assertEquals("Login successful", body.getMessage());
            assertNotNull(body.getTimestamp());
            assertEquals(expectedResponse, body.getData());
            
            verify(authService).login(loginDto);
        }

        @Test
        void logoutReturnsOkStatus() {
            ResponseEntity<ApiResponseDto<Void>> response = authController.logout(httpServletRequest);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ApiResponseDto<Void> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK.value(), body.getStatus());
            assertEquals("Logged out successfully", body.getMessage());
            assertNotNull(body.getTimestamp());
            assertNull(body.getData());
            
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
            
            ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.OK.value(), body.getStatus());
            assertEquals("Token verified successfully", body.getMessage());
            assertNotNull(body.getTimestamp());
            assertEquals(expectedResponse, body.getData());
            
            verify(authService).verifyToken(VALID_TOKEN);
        }

        @Test
        void verifyTokenInvalidTokenReturnsUnauthorizedStatus() {
            TokenVerificationResponseDto expectedResponse = TokenVerificationResponseDto.builder()
                    .valid(false)
                    .build();
            
            setupTokenVerification(INVALID_TOKEN, expectedResponse);
            
            ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
            assertEquals("Invalid or expired token", body.getMessage());
            assertNotNull(body.getTimestamp());
            
            verify(authService).verifyToken(INVALID_TOKEN);
        }

        @Test
        void verifyTokenNoAuthHeaderReturnsUnauthorizedStatus() {
            when(httpServletRequest.getHeader(AUTH_HEADER)).thenReturn(null);
            
            ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
            assertEquals("Invalid authentication token", body.getMessage());
            assertNotNull(body.getTimestamp());
            
            verifyNoInteractions(authService);
        }

        @Test
        void verifyTokenInvalidAuthHeaderFormatReturnsUnauthorizedStatus() {
            when(httpServletRequest.getHeader(AUTH_HEADER)).thenReturn("InvalidFormat token");
            
            ResponseEntity<ApiResponseDto<TokenVerificationResponseDto>> response = authController.verifyToken(httpServletRequest);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            ApiResponseDto<TokenVerificationResponseDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(HttpStatus.UNAUTHORIZED.value(), body.getStatus());
            assertEquals("Invalid authentication token", body.getMessage());
            assertNotNull(body.getTimestamp());
            
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
    }
}