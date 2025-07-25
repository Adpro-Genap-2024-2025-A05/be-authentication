package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.PasswordChangeDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UpdateProfileDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UserProfileDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final Counter profileViewCounter;
    private final Counter profileUpdateSuccessCounter;
    private final Counter profileUpdateFailureCounter;
    private final Counter passwordChangeSuccessCounter;
    private final Counter passwordChangeFailureCounter;
    private final Timer profileUpdateTimer;

    @Override
    public UserProfileDto getUserProfile(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            profileViewCounter.increment();

            if (user.getRole() == Role.CAREGIVER) {
                Caregiver caregiver = caregiverRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Caregiver not found"));
                return buildUserProfileDto(caregiver);
            } else if (user.getRole() == Role.PACILIAN) {
                Pacilian pacilian = pacilianRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacilian not found"));
                return buildUserProfileDto(pacilian);
            } else {
                return buildUserProfileDto(user);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(UpdateProfileDto updateProfileDto, Authentication authentication) {
        Timer.Sample sample = Timer.start();
        
        try {
            User user = (User) authentication.getPrincipal();

            if (updateProfileDto.getName() != null && !updateProfileDto.getName().isEmpty()) {
                user.setName(updateProfileDto.getName());
            }

            if (updateProfileDto.getAddress() != null && !updateProfileDto.getAddress().isEmpty()) {
                user.setAddress(updateProfileDto.getAddress());
            }

            if (updateProfileDto.getPhoneNumber() != null && !updateProfileDto.getPhoneNumber().isEmpty()) {
                user.setPhoneNumber(updateProfileDto.getPhoneNumber());
            }

            userRepository.save(user);

            UserProfileDto result;
            
            if (user.getRole() == Role.PACILIAN) {
                Pacilian pacilian = pacilianRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacilian not found"));

                if (updateProfileDto.getMedicalHistory() != null) {
                    pacilian.setMedicalHistory(updateProfileDto.getMedicalHistory());
                }

                pacilianRepository.save(pacilian);
                result = buildUserProfileDto(pacilian);

            } else if (user.getRole() == Role.CAREGIVER) {
                Caregiver caregiver = caregiverRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Caregiver not found"));

                if (updateProfileDto.getSpeciality() != null) {
                    Speciality.validatespeciality(updateProfileDto.getSpeciality());
                    caregiver.setSpeciality(updateProfileDto.getSpeciality());
                }

                if (updateProfileDto.getWorkAddress() != null && !updateProfileDto.getWorkAddress().isEmpty()) {
                    caregiver.setWorkAddress(updateProfileDto.getWorkAddress());
                }

                caregiverRepository.save(caregiver);
                result = buildUserProfileDto(caregiver);
            } else {
                result = buildUserProfileDto(user);
            }

            profileUpdateSuccessCounter.increment();
            sample.stop(profileUpdateTimer);
            
            return result;
            
        } catch (Exception e) {
            profileUpdateFailureCounter.increment();
            sample.stop(profileUpdateTimer);
            throw e;
        }
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
                passwordChangeFailureCounter.increment();
                throw new IllegalArgumentException("Current password is incorrect");
            }

            if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
                passwordChangeFailureCounter.increment();
                throw new IllegalArgumentException("New password and confirm password do not match");
            }

            user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            userRepository.save(user);
            
            passwordChangeSuccessCounter.increment();
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            passwordChangeFailureCounter.increment();
            throw e;
        }
    }

    private UserProfileDto buildUserProfileDto(Caregiver caregiver) {
        return UserProfileDto.builder()
                .id(caregiver.getId())
                .email(caregiver.getEmail())
                .name(caregiver.getName())
                .nik(caregiver.getNik())
                .phoneNumber(caregiver.getPhoneNumber())
                .role(caregiver.getRole())
                .address(caregiver.getAddress())
                .speciality(caregiver.getSpeciality())
                .workAddress(caregiver.getWorkAddress())
                .build();
    }

    private UserProfileDto buildUserProfileDto(Pacilian pacilian) {
        return UserProfileDto.builder()
                .id(pacilian.getId())
                .email(pacilian.getEmail())
                .name(pacilian.getName())
                .nik(pacilian.getNik())
                .address(pacilian.getAddress())
                .phoneNumber(pacilian.getPhoneNumber())
                .role(pacilian.getRole())
                .medicalHistory(pacilian.getMedicalHistory())
                .build();
    }

    private UserProfileDto buildUserProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nik(user.getNik())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}