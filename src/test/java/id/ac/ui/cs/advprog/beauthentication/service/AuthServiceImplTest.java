package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.*;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PacilianRepository pacilianRepository;

    @Mock
    private CaregiverRepository caregiverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String TEST_EMAIL = "patient@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_ENCODED_PASSWORD = "encoded_password";
    private static final String TEST_NAME = "Patient Name";
    private static final String TEST_NIK = "1234567890123456";
    private static final String TEST_ADDRESS = "Patient Address";
    private static final String TEST_PHONE = "1234567890";
    private static final String TEST_MEDICAL_HISTORY = "Some medical history";
    private static final String TEST_WORK_ADDRESS = "Work Address";
    private static final String TEST_SPECIALITY = "Cardiology";
    private static final String SUCCESS_MESSAGE = "Registration successful. Please login.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        when(passwordEncoder.encode(anyString())).thenReturn(TEST_ENCODED_PASSWORD);
    }

    @Nested
    class RegisterPacilianTests {
        @Test
        void validDataReturnsSuccessResponse() {
            RegisterPacilianDto registerDto = createPacilianDto();
            Pacilian savedPacilian = createSavedPacilian("patient-id");

            mockRepositoryValidations(false, false);
            when(pacilianRepository.save(any(Pacilian.class))).thenReturn(savedPacilian);

            RegisterResponseDto response = authService.registerPacilian(registerDto);

            assertRegistrationResponse(response, "patient-id", Role.PACILIAN);
            verifyRepositoryValidations(registerDto.getEmail(), registerDto.getNik());
            verify(passwordEncoder).encode(registerDto.getPassword());
            verify(pacilianRepository).save(any(Pacilian.class));
        }

        @Test
        void emailExistsThrowsIllegalArgumentException() {
            RegisterPacilianDto registerDto = createPacilianDto();
            registerDto.setEmail("existing@example.com");

            mockRepositoryValidations(true, false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.registerPacilian(registerDto));

            assertEquals("Email already exists", exception.getMessage());
            verify(userRepository).existsByEmail(registerDto.getEmail());
            verify(userRepository, never()).existsByNik(anyString());
            verify(pacilianRepository, never()).save(any(Pacilian.class));
        }

        @Test
        void nikExistsThrowsIllegalArgumentException() {
            RegisterPacilianDto registerDto = createPacilianDto();
            registerDto.setNik("existing_nik");

            mockRepositoryValidations(false, true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.registerPacilian(registerDto));

            assertEquals("NIK already exists", exception.getMessage());
            verify(userRepository).existsByEmail(registerDto.getEmail());
            verify(userRepository).existsByNik(registerDto.getNik());
            verify(pacilianRepository, never()).save(any(Pacilian.class));
        }
    }

    @Nested
    class RegisterCaregiverTests {
        @Test
        void validDataReturnsSuccessResponse() {
            RegisterCaregiverDto registerDto = createCaregiverDto();
            Caregiver savedCaregiver = createSavedCaregiver("caregiver-id");

            mockRepositoryValidations(false, false);
            when(caregiverRepository.save(any(Caregiver.class))).thenReturn(savedCaregiver);

            RegisterResponseDto response = authService.registerCaregiver(registerDto);

            assertRegistrationResponse(response, "caregiver-id", Role.CAREGIVER);
            verifyRepositoryValidations(registerDto.getEmail(), registerDto.getNik());
            verify(passwordEncoder).encode(registerDto.getPassword());
            verify(caregiverRepository).save(any(Caregiver.class));
        }

        @Test
        void emailExistsThrowsIllegalArgumentException() {
            RegisterCaregiverDto registerDto = createCaregiverDto();
            registerDto.setEmail("existing@example.com");

            mockRepositoryValidations(true, false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.registerCaregiver(registerDto));

            assertEquals("Email already exists", exception.getMessage());
            verify(userRepository).existsByEmail(registerDto.getEmail());
            verify(userRepository, never()).existsByNik(anyString());
            verify(caregiverRepository, never()).save(any(Caregiver.class));
        }

        @Test
        void nikExistsThrowsIllegalArgumentException() {
            RegisterCaregiverDto registerDto = createCaregiverDto();
            registerDto.setNik("existing_nik");

            mockRepositoryValidations(false, true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> authService.registerCaregiver(registerDto));

            assertEquals("NIK already exists", exception.getMessage());
            verify(userRepository).existsByEmail(registerDto.getEmail());
            verify(userRepository).existsByNik(registerDto.getNik());
            verify(caregiverRepository, never()).save(any(Caregiver.class));
        }
    }

    @Nested
    class LoginTests {
        @Test
        void validCredentialsReturnsLoginResponse() {
            LoginDto loginDto = createLoginDto();
            User user = createTestUser("user-id");
            String jwtToken = "jwt-token";
            long expirationTime = 3600L;

            mockAuthentication(user);
            when(jwtService.generateToken(user)).thenReturn(jwtToken);
            when(jwtService.getExpirationTime()).thenReturn(expirationTime);

            LoginResponseDto response = authService.login(loginDto);

            assertLoginResponse(response, jwtToken, user, expirationTime);
            verifyAuthentication(loginDto);
            verify(jwtService).generateToken(user);
            verify(jwtService).getExpirationTime();
            verify(securityContext).setAuthentication(authentication);
        }
    }

    @Nested
    class LogoutTests {
        @Test
        void logoutClearsSecurityContext() {
            try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
                authService.logout();

                mockedStatic.verify(SecurityContextHolder::clearContext);
            }
        }
    }

    @Nested
    class TokenVerificationTests {
        @Test
        void validTokenReturnsValidResponse() {
            String token = "valid-token";
            String userEmail = "user@example.com";
            User user = createTestUser("user-id");
            user.setEmail(userEmail);
            long remainingTime = 3000L;

            mockTokenValidation(token, userEmail, user, true, remainingTime);

            TokenVerificationResponseDto response = authService.verifyToken(token);

            assertValidTokenResponse(response, user, remainingTime);
            verifyTokenValidation(token, userEmail, user);
            verify(jwtService).getRemainingTime(token);
        }

        @Test
        void invalidTokenReturnsInvalidResponse() {
            String token = "invalid-token";
            String userEmail = "user@example.com";
            User user = createTestUser("user-id");
            user.setEmail(userEmail);

            mockTokenValidation(token, userEmail, user, false, 0L);

            TokenVerificationResponseDto response = authService.verifyToken(token);

            assertInvalidTokenResponse(response);
            verifyTokenValidation(token, userEmail, user);
            verify(jwtService, never()).getRemainingTime(anyString());
        }

        @Test
        void userNotFoundReturnsInvalidResponse() {
            String token = "valid-token";
            String userEmail = "nonexistent@example.com";

            when(jwtService.extractUsername(token)).thenReturn(userEmail);
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

            TokenVerificationResponseDto response = authService.verifyToken(token);

            assertInvalidTokenResponse(response);
            verify(jwtService).extractUsername(token);
            verify(userRepository).findByEmail(userEmail);
            verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
            verify(jwtService, never()).getRemainingTime(anyString());
        }

        @Test
        void nullUsernameReturnsInvalidResponse() {
            String token = "invalid-token";

            when(jwtService.extractUsername(token)).thenReturn(null);

            TokenVerificationResponseDto response = authService.verifyToken(token);

            assertInvalidTokenResponse(response);
            verify(jwtService).extractUsername(token);
            verifyNoInteractions(userRepository);
            verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
            verify(jwtService, never()).getRemainingTime(anyString());
        }

        @Test
        void exceptionThrownReturnsInvalidResponse() {
            String token = "exception-token";

            when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Token error"));

            TokenVerificationResponseDto response = authService.verifyToken(token);

            assertInvalidTokenResponse(response);
            verify(jwtService).extractUsername(token);
            verifyNoInteractions(userRepository);
            verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
            verify(jwtService, never()).getRemainingTime(anyString());
        }
    }

    private RegisterPacilianDto createPacilianDto() {
        return RegisterPacilianDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .address(TEST_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .medicalHistory(TEST_MEDICAL_HISTORY)
                .build();
    }

    private RegisterCaregiverDto createCaregiverDto() {
        return RegisterCaregiverDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .workAddress(TEST_WORK_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .speciality(TEST_SPECIALITY)
                .build();
    }

    private LoginDto createLoginDto() {
        return LoginDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
    }

    private User createTestUser(String id) {
        return User.builder()
                .id(id)
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .role(Role.PACILIAN)
                .build();
    }

    private Pacilian createSavedPacilian(String id) {
        return Pacilian.builder()
                .id(id)
                .email(TEST_EMAIL)
                .password(TEST_ENCODED_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .address(TEST_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .medicalHistory(TEST_MEDICAL_HISTORY)
                .role(Role.PACILIAN)
                .build();
    }

    private Caregiver createSavedCaregiver(String id) {
        return Caregiver.builder()
                .id(id)
                .email(TEST_EMAIL)
                .password(TEST_ENCODED_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .address(TEST_WORK_ADDRESS)
                .workAddress(TEST_WORK_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .speciality(TEST_SPECIALITY)
                .role(Role.CAREGIVER)
                .build();
    }

    private void mockRepositoryValidations(boolean emailExists, boolean nikExists) {
        when(userRepository.existsByEmail(anyString())).thenReturn(emailExists);
        when(userRepository.existsByNik(anyString())).thenReturn(nikExists);
    }

    private void mockAuthentication(User user) {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    private void mockTokenValidation(String token, String userEmail, User user, boolean isValid, long remainingTime) {
        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(isValid);

        if (isValid) {
            when(jwtService.getRemainingTime(token)).thenReturn(remainingTime);
        }
    }

    private void verifyRepositoryValidations(String email, String nik) {
        verify(userRepository).existsByEmail(email);
        verify(userRepository).existsByNik(nik);
    }

    private void verifyAuthentication(LoginDto loginDto) {
        verify(authenticationManager).authenticate(argThat(auth -> auth.getPrincipal().equals(loginDto.getEmail()) &&
                auth.getCredentials().equals(loginDto.getPassword())));
        verify(authentication).getPrincipal();
    }

    private void verifyTokenValidation(String token, String userEmail, User user) {
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByEmail(userEmail);
        verify(jwtService).isTokenValid(token, user);
    }

    private void assertRegistrationResponse(RegisterResponseDto response, String id, Role role) {
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(role, response.getRole());
        assertEquals(SUCCESS_MESSAGE, response.getMessage());
    }

    private void assertLoginResponse(LoginResponseDto response, String token, User user, long expiresIn) {
        assertNotNull(response);
        assertEquals(token, response.getAccessToken());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getRole(), response.getRole());
        assertEquals(expiresIn, response.getExpiresIn());
    }

    private void assertValidTokenResponse(TokenVerificationResponseDto response, User user, long expiresIn) {
        assertTrue(response.isValid());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getRole(), response.getRole());
        assertEquals(expiresIn, response.getExpiresIn());
    }

    private void assertInvalidTokenResponse(TokenVerificationResponseDto response) {
        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getExpiresIn());
    }
}