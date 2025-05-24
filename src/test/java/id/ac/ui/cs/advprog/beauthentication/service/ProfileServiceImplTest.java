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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PacilianRepository pacilianRepository;

    @Mock
    private CaregiverRepository caregiverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User testUser;
    private Pacilian testPacilian;
    private Caregiver testCaregiver;
    private UpdateProfileDto updateProfileDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("test-id")
                .email("test@example.com")
                .password("encoded-password")
                .name("Test User")
                .nik("1234567890123456")
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .build();

        testPacilian = Pacilian.builder()
                .id("test-id")
                .email("test@example.com")
                .password("encoded-password")
                .name("Test User")
                .nik("1234567890123456")
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .medicalHistory("Test medical history")
                .build();

        testCaregiver = Caregiver.builder()
                .id("test-id")
                .email("test@example.com")
                .password("encoded-password")
                .name("Test User")
                .nik("1234567890123456")
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.CAREGIVER)
                .speciality(Speciality.DOKTER_UMUM)
                .workAddress("Test work address")
                .build();

        updateProfileDto = new UpdateProfileDto();
        updateProfileDto.setName("New Name");
        updateProfileDto.setAddress("New Address");
        updateProfileDto.setPhoneNumber("9876543210");
        updateProfileDto.setMedicalHistory("New medical history");
        updateProfileDto.setSpeciality(Speciality.DOKTER_UMUM);
        updateProfileDto.setWorkAddress("New work address");
    }

    @Test
    void getUserProfileWhenUserIsPacilianShouldReturnProfileWithMedicalHistory() {
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));

        UserProfileDto result = profileService.getUserProfile(authentication);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getNik(), result.getNik());
        assertEquals(testUser.getAddress(), result.getAddress());
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testUser.getRole(), result.getRole());
        assertEquals(testPacilian.getMedicalHistory(), result.getMedicalHistory());
        assertNull(result.getSpeciality());
        assertNull(result.getWorkAddress());
    }

    @Test
    void getUserProfileWhenUserIsCaregiverShouldReturnProfileWithSpecialityAndWorkAddress() {
        testUser.setRole(Role.CAREGIVER);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));

        UserProfileDto result = profileService.getUserProfile(authentication);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getNik(), result.getNik());
        assertEquals(testUser.getAddress(), result.getAddress());
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testUser.getRole(), result.getRole());
        assertNull(result.getMedicalHistory());
        assertEquals(testCaregiver.getSpeciality(), result.getSpeciality());
        assertEquals(testCaregiver.getWorkAddress(), result.getWorkAddress());
    }

    @Test
    void getUserProfileWhenPacilianNotFoundShouldThrowException() {
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            profileService.getUserProfile(authentication);
        });
    }

    @Test
    void getUserProfileWhenCaregiverNotFoundShouldThrowException() {
        testUser.setRole(Role.CAREGIVER);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            profileService.getUserProfile(authentication);
        });
    }

    @Test
    void updateUserProfileWhenUserIsPacilianShouldUpdateUserAndPacilianProfile() {
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));

        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(updateProfileDto.getName(), result.getName());
        assertEquals(updatedUser.getNik(), result.getNik());
        assertEquals(updateProfileDto.getAddress(), result.getAddress());
        assertEquals(updateProfileDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedUser.getRole(), result.getRole());
        
        verify(userRepository).save(any(User.class));
        verify(pacilianRepository).save(any(Pacilian.class));
    }

    @Test
    void updateUserProfileWhenUserIsCaregiverShouldUpdateUserAndCaregiverProfile() {
        testUser.setRole(Role.CAREGIVER);

        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));

        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(updateProfileDto.getName(), result.getName());
        assertEquals(updatedUser.getNik(), result.getNik());
        assertEquals(updateProfileDto.getAddress(), result.getAddress());
        assertEquals(updateProfileDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedUser.getRole(), result.getRole());
        
        verify(userRepository).save(any(User.class));
        verify(caregiverRepository).save(any(Caregiver.class));
    }

    @Test
    void updateUserProfileWhenNameNullShouldNotUpdateName() {
        updateProfileDto.setName(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(testUser.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getName(), result.getName());
    }

    @Test
    void updateUserProfileWhenNameEmptyShouldNotUpdateName() {
        updateProfileDto.setName("");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(testUser.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getName(), result.getName());
    }

    @Test
    void updateUserProfileWhenAddressNullShouldNotUpdateAddress() {
        updateProfileDto.setAddress(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(testUser.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getAddress(), result.getAddress());
    }

    @Test
    void updateUserProfileWhenAddressEmptyShouldNotUpdateAddress() {
        updateProfileDto.setAddress("");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(testUser.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getAddress(), result.getAddress());
    }

    @Test
    void updateUserProfileWhenPhoneNumberNullShouldNotUpdatePhoneNumber() {
        updateProfileDto.setPhoneNumber(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(testUser.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void updateUserProfileWhenPhoneNumberEmptyShouldNotUpdatePhoneNumber() {
        updateProfileDto.setPhoneNumber("");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(testUser.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(updateProfileDto, authentication);
        
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void updateUserProfileWithOnlyNameShouldOnlyUpdateName() {
        UpdateProfileDto nameOnlyDto = new UpdateProfileDto();
        nameOnlyDto.setName("New Name Only");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(nameOnlyDto.getName())
                .nik(testUser.getNik())
                .address(testUser.getAddress())
                .phoneNumber(testUser.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(nameOnlyDto, authentication);
        
        assertEquals(nameOnlyDto.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getAddress(), result.getAddress());
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
    }
    
    @Test
    void updateUserProfileWithOnlyAddressShouldOnlyUpdateAddress() {
        UpdateProfileDto addressOnlyDto = new UpdateProfileDto();
        addressOnlyDto.setAddress("New Address Only");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(testUser.getName())
                .nik(testUser.getNik())
                .address(addressOnlyDto.getAddress())
                .phoneNumber(testUser.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(addressOnlyDto, authentication);
        
        assertEquals(addressOnlyDto.getAddress(), result.getAddress());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getPhoneNumber(), result.getPhoneNumber());
    }
    
    @Test
    void updateUserProfileWithOnlyPhoneNumberShouldOnlyUpdatePhoneNumber() {
        UpdateProfileDto phoneOnlyDto = new UpdateProfileDto();
        phoneOnlyDto.setPhoneNumber("9876543210");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(testUser.getName())
                .nik(testUser.getNik())
                .address(testUser.getAddress())
                .phoneNumber(phoneOnlyDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        UserProfileDto result = profileService.updateUserProfile(phoneOnlyDto, authentication);
        
        assertEquals(phoneOnlyDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getAddress(), result.getAddress());
    }

    @Test
    void updateUserProfileWhenCaregiverWithNullSpecialityShouldNotUpdateSpeciality() {
        testUser.setRole(Role.CAREGIVER);
        updateProfileDto.setSpeciality(null);
        updateProfileDto.setWorkAddress("New Work Address");
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));

        profileService.updateUserProfile(updateProfileDto, authentication);

        verify(caregiverRepository).save(any(Caregiver.class));
    }

    @Test
    void updateUserProfileWhenCaregiverWithNullWorkAddressShouldNotUpdateWorkAddress() {
        testUser.setRole(Role.CAREGIVER);
        updateProfileDto.setSpeciality(Speciality.DOKTER_UMUM);
        updateProfileDto.setWorkAddress(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));

        profileService.updateUserProfile(updateProfileDto, authentication);

        verify(caregiverRepository).save(any(Caregiver.class));
    }

    @Test
    void updateUserProfileWhenNeitherSpecialityNorWorkAddressIsProvidedShouldNotSaveCaregiver() {
        testUser.setRole(Role.CAREGIVER);
        updateProfileDto.setSpeciality(null);
        updateProfileDto.setWorkAddress(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));

        profileService.updateUserProfile(updateProfileDto, authentication);
        verify(caregiverRepository, never()).save(any(Caregiver.class));
    }

    @Test
    void buildUserProfileShouldCallRepositoriesBasedOnUserRole() {
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));

        profileService.getUserProfile(authentication);
        
        verify(pacilianRepository).findById(testUser.getId());
        verify(caregiverRepository, never()).findById(any());
        
        testUser.setRole(Role.CAREGIVER);
        when(caregiverRepository.findById(testUser.getId())).thenReturn(Optional.of(testCaregiver));
        
        profileService.getUserProfile(authentication);
        
        verify(caregiverRepository).findById(testUser.getId());
    }

    @Test
    void updateUserProfileWhenPacilianWithNullMedicalHistoryShouldNotUpdateMedicalHistory() {
        updateProfileDto.setMedicalHistory(null);
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(updateProfileDto.getName())
                .nik(testUser.getNik())
                .address(updateProfileDto.getAddress())
                .phoneNumber(updateProfileDto.getPhoneNumber())
                .role(testUser.getRole())
                .build();

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));

        profileService.updateUserProfile(updateProfileDto, authentication);

        verify(pacilianRepository, never()).save(any(Pacilian.class));
    }

    @Test
    void updateUserProfileWhenEmptyUpdateDtoShouldOnlySaveUser() {
        UpdateProfileDto emptyDto = new UpdateProfileDto();
        
        User updatedUser = User.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .name(testUser.getName())
                .nik(testUser.getNik())
                .address(testUser.getAddress())
                .phoneNumber(testUser.getPhoneNumber())
                .role(testUser.getRole())
                .build();
                
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(pacilianRepository.findById(testUser.getId())).thenReturn(Optional.of(testPacilian));
        
        profileService.updateUserProfile(emptyDto, authentication);
        
        verify(userRepository).save(any(User.class));
        verify(pacilianRepository, never()).save(any(Pacilian.class));
    }

    @Test
    void deleteUserAccountWhenUserIsPacilianShouldDeletePacilian() {
        when(authentication.getPrincipal()).thenReturn(testUser);

        profileService.deleteUserAccount(authentication);

        verify(pacilianRepository).deleteById(testUser.getId());
        verify(userRepository).deleteById(testUser.getId());
    }

    @Test
    void deleteUserAccountWhenUserIsCaregiverShouldDeleteCaregiver() {
        testUser.setRole(Role.CAREGIVER);
        when(authentication.getPrincipal()).thenReturn(testUser);

        profileService.deleteUserAccount(authentication);

        verify(caregiverRepository).deleteById(testUser.getId());
        verify(userRepository).deleteById(testUser.getId());
    }

    @Test
    void changePasswordWhenCurrentPasswordDoesNotMatchShouldThrowException() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setCurrentPassword("wrongPassword");
        passwordChangeDto.setNewPassword("newPassword");
        passwordChangeDto.setConfirmPassword("newPassword");

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            profileService.changePassword(passwordChangeDto, authentication);
        });
        
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePasswordWhenNewPasswordAndConfirmPasswordDoNotMatchShouldThrowException() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setCurrentPassword("currentPassword");
        passwordChangeDto.setNewPassword("newPassword");
        passwordChangeDto.setConfirmPassword("differentPassword");

        when(authentication.getPrincipal()).thenReturn(testUser);
        when(passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), testUser.getPassword())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            profileService.changePassword(passwordChangeDto, authentication);
        });
        
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void changePasswordWhenValidShouldUpdatePassword() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setCurrentPassword("currentPassword");
        passwordChangeDto.setNewPassword("newPassword");
        passwordChangeDto.setConfirmPassword("newPassword");
        
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordChangeDto.getNewPassword())).thenReturn("encoded-new-password");
        
        profileService.changePassword(passwordChangeDto, authentication);
        
        verify(userRepository).save(testUser);
        assertEquals("encoded-new-password", testUser.getPassword());
    }
}