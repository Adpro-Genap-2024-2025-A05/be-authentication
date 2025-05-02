package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenVerificationResponseDtoTest {

    @Test
    void builderCreatesTokenVerificationResponseDto() {
        TokenVerificationResponseDto dto = TokenVerificationResponseDto.builder()
                .valid(true)
                .userId("user-id")
                .email("user@example.com")
                .role(Role.PACILIAN)
                .expiresIn(3600L)
                .build();
        
        assertTrue(dto.isValid());
        assertEquals("user-id", dto.getUserId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void noArgsConstructorCreatesEmptyTokenVerificationResponseDto() {
        TokenVerificationResponseDto dto = new TokenVerificationResponseDto();
        
        assertFalse(dto.isValid()); 
        assertNull(dto.getUserId());
        assertNull(dto.getEmail());
        assertNull(dto.getRole());
        assertNull(dto.getExpiresIn());
    }

    @Test
    void allArgsConstructorCreatesTokenVerificationResponseDto() {
        TokenVerificationResponseDto dto = new TokenVerificationResponseDto(
                true, "user-id", "user@example.com", Role.PACILIAN, 3600L);
        
        assertTrue(dto.isValid());
        assertEquals("user-id", dto.getUserId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        TokenVerificationResponseDto dto = new TokenVerificationResponseDto();
        
        dto.setValid(true);
        dto.setUserId("user-id");
        dto.setEmail("user@example.com");
        dto.setRole(Role.PACILIAN);
        dto.setExpiresIn(3600L);
        
        assertTrue(dto.isValid());
        assertEquals("user-id", dto.getUserId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals(3600L, dto.getExpiresIn());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        TokenVerificationResponseDto dto1 = new TokenVerificationResponseDto(
                true, "user-id", "user@example.com", Role.PACILIAN, 3600L);
        
        TokenVerificationResponseDto dto2 = new TokenVerificationResponseDto(
                true, "user-id", "user@example.com", Role.PACILIAN, 3600L);
        
        TokenVerificationResponseDto dto3 = new TokenVerificationResponseDto(
                false, "different-id", "different@example.com", Role.CAREGIVER, 7200L);
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}