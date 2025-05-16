package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.PasswordChangeDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UpdateProfileDto;
import id.ac.ui.cs.advprog.beauthentication.dto.UserProfileDto;
import org.springframework.security.core.Authentication;

public interface ProfileService {
    UserProfileDto getUserProfile(Authentication authentication);
    UserProfileDto updateUserProfile(UpdateProfileDto updateProfileDto, Authentication authentication);
    void deleteUserAccount(Authentication authentication);
    void changePassword(PasswordChangeDto passwordChangeDto, Authentication authentication);
}