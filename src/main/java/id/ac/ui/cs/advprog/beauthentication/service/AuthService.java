package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.*;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponseDto registerPacilian(RegisterPacilianDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByNik(registerDto.getNik())) {
            throw new IllegalArgumentException("NIK already exists");
        }

        Pacilian pacilian = Pacilian.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .address(registerDto.getAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .medicalHistory(registerDto.getMedicalHistory())
                .role(Role.PACILIAN)
                .build();

        Pacilian savedPacilian = pacilianRepository.save(pacilian);

        return RegisterResponseDto.builder()
                .id(savedPacilian.getId())
                .role(savedPacilian.getRole())
                .message("Registration successful. Please login.")
                .build();
    }

    @Transactional
    public RegisterResponseDto registerCaregiver(RegisterCaregiverDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByNik(registerDto.getNik())) {
            throw new IllegalArgumentException("NIK already exists");
        }

        Caregiver caregiver = Caregiver.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .nik(registerDto.getNik())
                .address(registerDto.getWorkAddress())
                .workAddress(registerDto.getWorkAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .speciality(registerDto.getSpeciality())
                .role(Role.CAREGIVER)
                .build();

        registerDto.getWorkingSchedules().forEach(scheduleDto -> {
            WorkingSchedule schedule = WorkingSchedule.builder()
                    .dayOfWeek(scheduleDto.getDayOfWeek())
                    .build();
            caregiver.addWorkingSchedule(schedule);
        });

        Caregiver savedCaregiver = caregiverRepository.save(caregiver);

        return RegisterResponseDto.builder()
                .id(savedCaregiver.getId())
                .role(savedCaregiver.getRole())
                .message("Registration successful. Please login.")
                .build();
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public TokenVerificationResponseDto verifyToken(String token) {
        try {
            String userEmail = jwtService.extractUsername(token);

            if (userEmail != null) {
                User user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                if (jwtService.isTokenValid(token, user)) {
                    return TokenVerificationResponseDto.builder()
                            .valid(true)
                            .userId(user.getId())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .expiresIn(jwtService.getRemainingTime(token))
                            .build();
                }
            }

            return TokenVerificationResponseDto.builder()
                    .valid(false)
                    .build();

        } catch (Exception e) {
            return TokenVerificationResponseDto.builder()
                    .valid(false)
                    .build();
        }
    }
}