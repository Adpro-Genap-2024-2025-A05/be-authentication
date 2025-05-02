package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.*;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

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
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void registerPacilianValidDataReturnsSuccessResponse() {
        RegisterPacilianDto registerDto = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByNik(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        Pacilian savedPacilian = Pacilian.builder()
                .id("patient-id")
                .email(registerDto.getEmail())
                .password("encoded_password")
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .address(registerDto.getAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .medicalHistory(registerDto.getMedicalHistory())
                .role(Role.PACILIAN)
                .build();

        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(savedPacilian);

        RegisterResponseDto response = authService.registerPacilian(registerDto);

        assertNotNull(response);
        assertEquals("patient-id", response.getId());
        assertEquals(Role.PACILIAN, response.getRole());
        assertEquals("Registration successful. Please login.", response.getMessage());

        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository).existsByNik(registerDto.getNik());
        verify(passwordEncoder).encode(registerDto.getPassword());
        verify(pacilianRepository).save(any(Pacilian.class));
    }

    @Test
    void registerPacilianEmailExistsThrowsIllegalArgumentException() {
        RegisterPacilianDto registerDto = RegisterPacilianDto.builder()
                .email("existing@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.registerPacilian(registerDto));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository, never()).existsByNik(anyString());
        verify(pacilianRepository, never()).save(any(Pacilian.class));
    }

    @Test
    void registerPacilianNikExistsThrowsIllegalArgumentException() {
        RegisterPacilianDto registerDto = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("existing_nik")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.existsByNik(registerDto.getNik())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.registerPacilian(registerDto));

        assertEquals("NIK already exists", exception.getMessage());
        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository).existsByNik(registerDto.getNik());
        verify(pacilianRepository, never()).save(any(Pacilian.class));
    }

    @Test
    void registerCaregiverValidDataReturnsSuccessResponse() {
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();

        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);

        RegisterCaregiverDto registerDto = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(scheduleDtos)
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByNik(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        Caregiver savedCaregiver = Caregiver.builder()
                .id("caregiver-id")
                .email(registerDto.getEmail())
                .password("encoded_password")
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .workAddress(registerDto.getWorkAddress())
                .address(registerDto.getWorkAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .speciality(registerDto.getSpeciality())
                .role(Role.CAREGIVER)
                .workingSchedules(new ArrayList<>())
                .build();

        when(caregiverRepository.save(any(Caregiver.class))).thenReturn(savedCaregiver);

        RegisterResponseDto response = authService.registerCaregiver(registerDto);

        assertNotNull(response);
        assertEquals("caregiver-id", response.getId());
        assertEquals(Role.CAREGIVER, response.getRole());
        assertEquals("Registration successful. Please login.", response.getMessage());

        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository).existsByNik(registerDto.getNik());
        verify(passwordEncoder).encode(registerDto.getPassword());
        verify(caregiverRepository).save(any(Caregiver.class));
    }

    @Test
    void registerCaregiverEmailExistsThrowsIllegalArgumentException() {
        RegisterCaregiverDto registerDto = RegisterCaregiverDto.builder()
                .email("existing@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(new ArrayList<>())
                .build();

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.registerCaregiver(registerDto));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository, never()).existsByNik(anyString());
        verify(caregiverRepository, never()).save(any(Caregiver.class));
    }

    @Test
    void registerCaregiverNikExistsThrowsIllegalArgumentException() {
        RegisterCaregiverDto registerDto = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("existing_nik")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(new ArrayList<>())
                .build();

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.existsByNik(registerDto.getNik())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.registerCaregiver(registerDto));

        assertEquals("NIK already exists", exception.getMessage());
        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(userRepository).existsByNik(registerDto.getNik());
        verify(caregiverRepository, never()).save(any(Caregiver.class));
    }

    @Test
    void loginValidCredentialsReturnsLoginResponse() {
        LoginDto loginDto = LoginDto.builder()
                .email("user@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .id("user-id")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .build();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        LoginResponseDto response = authService.login(loginDto);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("user@example.com", response.getEmail());
        assertEquals("User Name", response.getName());
        assertEquals(Role.PACILIAN, response.getRole());
        assertEquals(3600L, response.getExpiresIn());

        verify(authenticationManager).authenticate(argThat(auth -> auth.getPrincipal().equals(loginDto.getEmail()) &&
                auth.getCredentials().equals(loginDto.getPassword())));
        verify(authentication).getPrincipal();
        verify(jwtService).generateToken(user);
        verify(jwtService).getExpirationTime();
        verify(securityContext).setAuthentication(authentication);
    }

    @Test
    void logoutClearsSecurityContext() {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            authService.logout();

            mockedStatic.verify(SecurityContextHolder::clearContext);
        }
    }

    @Test
    void verifyTokenValidTokenReturnsValidResponse() {
        String token = "valid-token";
        String userEmail = "user@example.com";

        User user = User.builder()
                .id("user-id")
                .email(userEmail)
                .role(Role.PACILIAN)
                .build();

        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(true);
        when(jwtService.getRemainingTime(token)).thenReturn(3000L);

        TokenVerificationResponseDto response = authService.verifyToken(token);

        assertTrue(response.isValid());
        assertEquals("user-id", response.getUserId());
        assertEquals(userEmail, response.getEmail());
        assertEquals(Role.PACILIAN, response.getRole());
        assertEquals(3000L, response.getExpiresIn());

        verify(jwtService).extractUsername(token);
        verify(userRepository).findByEmail(userEmail);
        verify(jwtService).isTokenValid(token, user);
        verify(jwtService).getRemainingTime(token);
    }

    @Test
    void verifyTokenInvalidTokenReturnsInvalidResponse() {
        String token = "invalid-token";
        String userEmail = "user@example.com";

        User user = User.builder()
                .id("user-id")
                .email(userEmail)
                .role(Role.PACILIAN)
                .build();

        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(false);

        TokenVerificationResponseDto response = authService.verifyToken(token);

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getExpiresIn());

        verify(jwtService).extractUsername(token);
        verify(userRepository).findByEmail(userEmail);
        verify(jwtService).isTokenValid(token, user);
        verify(jwtService, never()).getRemainingTime(anyString());
    }

    @Test
    void verifyTokenUserNotFoundReturnsInvalidResponse() {
        String token = "valid-token";
        String userEmail = "nonexistent@example.com";

        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        TokenVerificationResponseDto response = authService.verifyToken(token);

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getExpiresIn());

        verify(jwtService).extractUsername(token);
        verify(userRepository).findByEmail(userEmail);
        verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
        verify(jwtService, never()).getRemainingTime(anyString());
    }

    @Test
    void verifyTokenNullUsernameReturnsInvalidResponse() {
        String token = "invalid-token";

        when(jwtService.extractUsername(token)).thenReturn(null);

        TokenVerificationResponseDto response = authService.verifyToken(token);

        assertFalse(response.isValid());

        verify(jwtService).extractUsername(token);
        verifyNoInteractions(userRepository);
        verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
        verify(jwtService, never()).getRemainingTime(anyString());
    }

    @Test
    void verifyTokenExceptionThrownReturnsInvalidResponse() {
        String token = "exception-token";

        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Token error"));

        TokenVerificationResponseDto response = authService.verifyToken(token);

        assertFalse(response.isValid());

        verify(jwtService).extractUsername(token);
        verifyNoInteractions(userRepository);
        verify(jwtService, never()).isTokenValid(anyString(), any(User.class));
        verify(jwtService, never()).getRemainingTime(anyString());
    }
}