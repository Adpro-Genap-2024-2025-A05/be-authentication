package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> getUserProfile(Authentication authentication) {
        UserProfileDto profile = profileService.getUserProfile(authentication);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                                "Profile retrieved successfully",
                                profile));
    }

    @PutMapping("")
    public ResponseEntity<ApiResponseDto<UserProfileDto>> updateUserProfile(
            @Valid @RequestBody UpdateProfileDto updateProfileDto,
            Authentication authentication) {
        UserProfileDto updatedProfile = profileService.updateUserProfile(updateProfileDto, authentication);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                                "Profile updated successfully",
                                updatedProfile));
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponseDto<Void>> deleteUserAccount(Authentication authentication) {
        profileService.deleteUserAccount(authentication);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                                "Account deleted successfully",
                                null));
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseDto<Void>> changePassword(
            @Valid @RequestBody PasswordChangeDto passwordChangeDto,
            Authentication authentication) {
        profileService.changePassword(passwordChangeDto, authentication);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                                "Password changed successfully",
                                null));
    }
}