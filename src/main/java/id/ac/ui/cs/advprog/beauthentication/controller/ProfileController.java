package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<UserProfileDto> getUserProfile(Authentication authentication) {
        UserProfileDto profile = profileService.getUserProfile(authentication);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @Valid @RequestBody UpdateProfileDto updateProfileDto,
            Authentication authentication) {
        UserProfileDto updatedProfile = profileService.updateUserProfile(updateProfileDto, authentication);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("")
    public ResponseEntity<Map<String, String>> deleteUserAccount(Authentication authentication) {
        profileService.deleteUserAccount(authentication);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody PasswordChangeDto passwordChangeDto,
            Authentication authentication) {
        profileService.changePassword(passwordChangeDto, authentication);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
}