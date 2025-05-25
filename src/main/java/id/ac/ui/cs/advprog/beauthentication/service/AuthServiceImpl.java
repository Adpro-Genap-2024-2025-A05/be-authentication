package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import id.ac.ui.cs.advprog.beauthentication.model.*;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final Counter registerPacilianCounter;
    private final Counter registerCaregiverCounter;
    private final Counter registerPacilianFailureCounter;
    private final Counter registerCaregiverFailureCounter;
    private final Timer tokenVerificationTimer;
    
    private static final String REGISTRATION_SUCCESS_MESSAGE = "Registration successful. Please login.";

    @Override
    @Transactional
    public RegisterResponseDto registerPacilian(RegisterPacilianDto registerDto) {
        try {
            validateRegistration(registerDto.getEmail(), registerDto.getNik());

            Pacilian pacilian = createPacilianEntity(registerDto);
            Pacilian savedPacilian = pacilianRepository.save(pacilian);

            registerPacilianCounter.increment();

            return buildRegisterResponse(savedPacilian.getId(), savedPacilian.getRole());
        } catch (Exception e) {
            registerPacilianFailureCounter.increment();
            throw e;
        }
    }

    @Override
    @Transactional
    public RegisterResponseDto registerCaregiver(RegisterCaregiverDto registerDto) {
        try {
            validateRegistration(registerDto.getEmail(), registerDto.getNik());

            Caregiver caregiver = createCaregiverEntity(registerDto);
            
            Caregiver savedCaregiver = caregiverRepository.save(caregiver);

            registerCaregiverCounter.increment();

            return buildRegisterResponse(savedCaregiver.getId(), savedCaregiver.getRole());
        } catch (Exception e) {
            registerCaregiverFailureCounter.increment();
            throw e;
        }
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticateUser(loginDto);
            User user = (User) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(user);

            loginSuccessCounter.increment();

            return buildLoginResponse(user, jwtToken);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            loginFailureCounter.increment();
            throw e;
        } catch (Exception e) {
            loginFailureCounter.increment();
            throw e;
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public TokenVerificationResponseDto verifyToken(String token) {
        try {
            return tokenVerificationTimer.recordCallable(() -> {
                try {
                    String userEmail = jwtService.extractUsername(token);
                    if (userEmail == null) {
                        return buildInvalidTokenResponse();
                    }

                    return verifyTokenForUser(token, userEmail);
                } catch (Exception e) {
                    return buildInvalidTokenResponse();
                }
            });
        } catch (Exception e) {
            return buildInvalidTokenResponse();
        }
    }
    
    private void validateRegistration(String email, String nik) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByNik(nik)) {
            throw new IllegalArgumentException("NIK already exists");
        }
    }
    
    private Pacilian createPacilianEntity(RegisterPacilianDto registerDto) {
        return Pacilian.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .address(registerDto.getAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .medicalHistory(registerDto.getMedicalHistory())
                .role(Role.PACILIAN)
                .build();
    }
    
    private Caregiver createCaregiverEntity(RegisterCaregiverDto registerDto) {
        Speciality.validatespeciality(registerDto.getSpeciality());
        
        return Caregiver.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .address(registerDto.getAddress())
                .workAddress(registerDto.getWorkAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .speciality(registerDto.getSpeciality())
                .role(Role.CAREGIVER)
                .build();
    }
    
    private RegisterResponseDto buildRegisterResponse(String id, Role role) {
        return RegisterResponseDto.builder()
                .id(id)
                .role(role)
                .message(REGISTRATION_SUCCESS_MESSAGE)
                .build();
    }
    
    private Authentication authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
                        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    
    private LoginResponseDto buildLoginResponse(User user, String jwtToken) {
        return LoginResponseDto.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
    
    private TokenVerificationResponseDto verifyTokenForUser(String token, String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(token, user)) {
                return buildValidTokenResponse(token, user);
            }
            
            return buildInvalidTokenResponse();
        } catch (UsernameNotFoundException e) {
            return buildInvalidTokenResponse();
        }
    }
    
    private TokenVerificationResponseDto buildValidTokenResponse(String token, User user) {
        return TokenVerificationResponseDto.builder()
                .valid(true)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .expiresIn(jwtService.getRemainingTime(token))
                .build();
    }
    
    private TokenVerificationResponseDto buildInvalidTokenResponse() {
        return TokenVerificationResponseDto.builder()
                .valid(false)
                .build();
    }

    public Caregiver getCaregiverByID(String id) {
        return caregiverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caregiver with ID " + id + " not found"));
    }

    public Pacilian getPacilianByID(String id) {
        return pacilianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pacilian with ID " + id + " not found"));
    }
}