package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.PasswordChangeDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UpdateProfileDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UserProfileDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.UserRepository;
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

    @Override
    public UserProfileDto getUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return buildUserProfileDto(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(UpdateProfileDto updateProfileDto, Authentication authentication) {
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
        
        if (user.getRole() == Role.PACILIAN) {
            Pacilian pacilian = pacilianRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacilian not found"));
            
            if (updateProfileDto.getMedicalHistory() != null) {
                pacilian.setMedicalHistory(updateProfileDto.getMedicalHistory());
                pacilianRepository.save(pacilian);
            }
        } else if (user.getRole() == Role.CAREGIVER) {
            Caregiver caregiver = caregiverRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Caregiver not found"));
            
            if (updateProfileDto.getSpeciality() != null && !updateProfileDto.getSpeciality().isEmpty()) {
                caregiver.setSpeciality(updateProfileDto.getSpeciality());
            }
            
            if (updateProfileDto.getWorkAddress() != null && !updateProfileDto.getWorkAddress().isEmpty()) {
                caregiver.setWorkAddress(updateProfileDto.getWorkAddress());
            }
            
            if (updateProfileDto.getSpeciality() != null || updateProfileDto.getWorkAddress() != null) {
                caregiverRepository.save(caregiver);
            }
        }
        
        User updatedUser = userRepository.save(user);
        return buildUserProfileDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        if (user.getRole() == Role.PACILIAN) {
            pacilianRepository.deleteById(user.getId());
        } else if (user.getRole() == Role.CAREGIVER) {
            caregiverRepository.deleteById(user.getId());
        }
        
        userRepository.deleteById(user.getId());
    }
    
    @Override
    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);
    }
    
    private UserProfileDto buildUserProfileDto(User user) {
        UserProfileDto profileDto = UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nik(user.getNik())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
        
        if (user.getRole() == Role.PACILIAN) {
            Pacilian pacilian = pacilianRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacilian not found"));
            
            profileDto.setMedicalHistory(pacilian.getMedicalHistory());
        } else if (user.getRole() == Role.CAREGIVER) {
            Caregiver caregiver = caregiverRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Caregiver not found"));
            
            profileDto.setSpeciality(caregiver.getSpeciality());
            profileDto.setWorkAddress(caregiver.getWorkAddress());
        }
        
        return profileDto;
    }
}