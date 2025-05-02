package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDtoTest {

    @Test
    void builderCreatesLoginResponseDto() {
        LoginResponseDto dto = LoginResponseDto.builder()
                .accessToken("jwt-token")
                .email("user@example.com")
                .name("User Name")
                .role(Role.PACILIAN)
                .expiresIn(3600L)
                .build();
        
        assertEquals("jwt-token", dto.getAccessToken());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("User Name", dto.getName());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void noArgsConstructorCreatesEmptyLoginResponseDto() {
        LoginResponseDto dto = new LoginResponseDto();
        
        assertNull(dto.getAccessToken());
        assertNull(dto.getEmail());
        assertNull(dto.getName());
        assertNull(dto.getRole());
        assertNull(dto.getExpiresIn());
    }

    @Test
    void allArgsConstructorCreatesLoginResponseDto() {
        LoginResponseDto dto = new LoginResponseDto(
                "jwt-token", "user@example.com", "User Name", Role.PACILIAN, 3600L);
        
        assertEquals("jwt-token", dto.getAccessToken());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("User Name", dto.getName());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        LoginResponseDto dto = new LoginResponseDto();
        
        dto.setAccessToken("jwt-token");
        dto.setEmail("user@example.com");
        dto.setName("User Name");
        dto.setRole(Role.PACILIAN);
        dto.setExpiresIn(3600L);
        
        assertEquals("jwt-token", dto.getAccessToken());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("User Name", dto.getName());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        LoginResponseDto dto1 = new LoginResponseDto(
                "jwt-token", "user@example.com", "User Name", Role.PACILIAN, 3600L);
        
        LoginResponseDto dto2 = new LoginResponseDto(
                "jwt-token", "user@example.com", "User Name", Role.PACILIAN, 3600L);
        
        LoginResponseDto dto3 = new LoginResponseDto(
                "different-token", "different@example.com", "Different Name", Role.CAREGIVER, 7200L);
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}