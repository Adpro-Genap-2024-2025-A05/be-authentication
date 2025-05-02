package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest {

    @Test
    void builderCreatesLoginDto() {
        LoginDto dto = LoginDto.builder()
                .email("user@example.com")
                .password("password")
                .build();
        
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
    }

    @Test
    void noArgsConstructorCreatesEmptyLoginDto() {
        LoginDto dto = new LoginDto();
        
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void allArgsConstructorCreatesLoginDto() {
        LoginDto dto = new LoginDto("user@example.com", "password");
        
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        LoginDto dto = new LoginDto();
        
        dto.setEmail("user@example.com");
        dto.setPassword("password");
        
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        LoginDto dto1 = new LoginDto("user@example.com", "password");
        LoginDto dto2 = new LoginDto("user@example.com", "password");
        LoginDto dto3 = new LoginDto("different@example.com", "password");
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}