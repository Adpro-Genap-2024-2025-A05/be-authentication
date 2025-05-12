package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest {

    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String DIFFERENT_EMAIL = "different@example.com";

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesLoginDto() {
            LoginDto dto = createTestDto();
            
            assertAllFieldsMatch(dto);
        }

        @Test
        void noArgsConstructorCreatesEmptyLoginDto() {
            LoginDto dto = new LoginDto();
            
            assertNull(dto.getEmail());
            assertNull(dto.getPassword());
        }

        @Test
        void allArgsConstructorCreatesLoginDto() {
            LoginDto dto = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            LoginDto dto = new LoginDto();
            
            dto.setEmail(TEST_EMAIL);
            dto.setPassword(TEST_PASSWORD);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            LoginDto dto1 = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
            LoginDto dto2 = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            LoginDto dto1 = new LoginDto(TEST_EMAIL, TEST_PASSWORD);
            LoginDto dto2 = new LoginDto(DIFFERENT_EMAIL, TEST_PASSWORD);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private LoginDto createTestDto() {
        return LoginDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();
    }
    
    private void assertAllFieldsMatch(LoginDto dto) {
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_PASSWORD, dto.getPassword());
    }
}