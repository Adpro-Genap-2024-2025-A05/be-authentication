package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.UserProfileDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(profileService.getUserProfile(any(Authentication.class)))
                .thenReturn(createUserProfileDto());
                
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    void getUserProfile_shouldReturnUserProfile() throws Exception {
        UserProfileDto userProfileDto = createUserProfileDto();
        doReturn(userProfileDto).when(profileService).getUserProfile(any());

        mockMvc.perform(get("/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userProfileDto.getId()))
                .andExpect(jsonPath("$.email").value(userProfileDto.getEmail()))
                .andExpect(jsonPath("$.name").value(userProfileDto.getName()))
                .andExpect(jsonPath("$.role").value(userProfileDto.getRole().toString()));
    }

    @Test
    void updateUserProfile_shouldReturnUpdatedProfile() throws Exception {
        UserProfileDto updatedProfile = createUserProfileDto();
        updatedProfile.setName("Updated Name");
        updatedProfile.setEmail("updated@example.com");

        doReturn(updatedProfile).when(profileService).updateUserProfile(any(), any());

        mockMvc.perform(put("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedProfile.getName()))
                .andExpect(jsonPath("$.email").value(updatedProfile.getEmail()));
    }

    @Test
    void deleteUserAccount_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(profileService).deleteUserAccount(any());

        mockMvc.perform(delete("/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Account deleted successfully"))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void changePassword_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(profileService).changePassword(any(), any());

        mockMvc.perform(post("/profile/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"oldPassword\",\"newPassword\":\"newPassword\",\"confirmPassword\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"))
                .andExpect(jsonPath("$.status").value("success"));
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