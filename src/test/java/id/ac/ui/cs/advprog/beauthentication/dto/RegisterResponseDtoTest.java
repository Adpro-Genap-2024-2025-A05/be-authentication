package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterResponseDtoTest {

    @Test
    void builderCreatesRegisterResponseDto() {
        RegisterResponseDto dto = RegisterResponseDto.builder()
                .id("user-id")
                .role(Role.PACILIAN)
                .message("Registration successful")
                .build();
        
        assertEquals("user-id", dto.getId());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals("Registration successful", dto.getMessage());
    }

    @Test
    void noArgsConstructorCreatesEmptyRegisterResponseDto() {
        RegisterResponseDto dto = new RegisterResponseDto();
        
        assertNull(dto.getId());
        assertNull(dto.getRole());
        assertNull(dto.getMessage());
    }

    @Test
    void allArgsConstructorCreatesRegisterResponseDto() {
        RegisterResponseDto dto = new RegisterResponseDto("user-id", Role.PACILIAN, "Registration successful");
        
        assertEquals("user-id", dto.getId());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals("Registration successful", dto.getMessage());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        RegisterResponseDto dto = new RegisterResponseDto();
        
        dto.setId("user-id");
        dto.setRole(Role.PACILIAN);
        dto.setMessage("Registration successful");
        
        assertEquals("user-id", dto.getId());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals("Registration successful", dto.getMessage());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        RegisterResponseDto dto1 = new RegisterResponseDto("user-id", Role.PACILIAN, "Registration successful");
        RegisterResponseDto dto2 = new RegisterResponseDto("user-id", Role.PACILIAN, "Registration successful");
        RegisterResponseDto dto3 = new RegisterResponseDto("different-id", Role.CAREGIVER, "Different message");
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}