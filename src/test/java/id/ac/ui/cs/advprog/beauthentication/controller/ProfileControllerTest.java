package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.*;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfile_shouldReturnUserProfile() {
        UserProfileDto expectedProfile = createUserProfileDto();
        when(profileService.getUserProfile(any(Authentication.class))).thenReturn(expectedProfile);

        ResponseEntity<ApiResponseDto<UserProfileDto>> response = profileController.getUserProfile(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<UserProfileDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK.value(), body.getStatus());
        assertEquals("Profile retrieved successfully", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertEquals(expectedProfile, body.getData());

        verify(profileService).getUserProfile(authentication);
    }

    @Test
    void updateUserProfile_shouldReturnUpdatedProfile() {
        UpdateProfileDto updateDto = new UpdateProfileDto();
        updateDto.setName("Updated Name");
        UserProfileDto updatedProfile = createUserProfileDto();
        updatedProfile.setName("Updated Name");
        
        when(profileService.updateUserProfile(any(UpdateProfileDto.class), any(Authentication.class)))
                .thenReturn(updatedProfile);

        ResponseEntity<ApiResponseDto<UserProfileDto>> response = 
                profileController.updateUserProfile(updateDto, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<UserProfileDto> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK.value(), body.getStatus());
        assertEquals("Profile updated successfully", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertEquals(updatedProfile, body.getData());

        verify(profileService).updateUserProfile(updateDto, authentication);
    }

    @Test
    void changePassword_shouldReturnSuccessMessage() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setCurrentPassword("current");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setConfirmPassword("new");
        
        doNothing().when(profileService).changePassword(any(PasswordChangeDto.class), any(Authentication.class));

        ResponseEntity<ApiResponseDto<Void>> response = 
                profileController.changePassword(passwordChangeDto, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponseDto<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.OK.value(), body.getStatus());
        assertEquals("Password changed successfully", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertNull(body.getData());

        verify(profileService).changePassword(passwordChangeDto, authentication);
    }

    private UserProfileDto createUserProfileDto() {
        return UserProfileDto.builder()
                .id("test-id")
                .email("test@example.com")
                .name("Test User")
                .nik("1234567890123456")
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .medicalHistory("Test medical history")
                .build();
    }
}