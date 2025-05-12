package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDtoTest {

    private static final String TEST_TOKEN = "jwt-token";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_NAME = "User Name";
    private static final Role TEST_ROLE = Role.PACILIAN;
    private static final Long TEST_EXPIRES = 3600L;
    private static final String DIFFERENT_TOKEN = "different-token";
    private static final String DIFFERENT_EMAIL = "different@example.com";
    private static final String DIFFERENT_NAME = "Different Name";
    private static final Role DIFFERENT_ROLE = Role.CAREGIVER;
    private static final Long DIFFERENT_EXPIRES = 7200L;

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesLoginResponseDto() {
            LoginResponseDto dto = createTestDto();
            
            assertAllFieldsMatch(dto);
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
                    TEST_TOKEN, TEST_EMAIL, TEST_NAME, TEST_ROLE, TEST_EXPIRES);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            LoginResponseDto dto = new LoginResponseDto();
            
            dto.setAccessToken(TEST_TOKEN);
            dto.setEmail(TEST_EMAIL);
            dto.setName(TEST_NAME);
            dto.setRole(TEST_ROLE);
            dto.setExpiresIn(TEST_EXPIRES);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            LoginResponseDto dto1 = new LoginResponseDto(
                    TEST_TOKEN, TEST_EMAIL, TEST_NAME, TEST_ROLE, TEST_EXPIRES);
            
            LoginResponseDto dto2 = new LoginResponseDto(
                    TEST_TOKEN, TEST_EMAIL, TEST_NAME, TEST_ROLE, TEST_EXPIRES);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            LoginResponseDto dto1 = new LoginResponseDto(
                    TEST_TOKEN, TEST_EMAIL, TEST_NAME, TEST_ROLE, TEST_EXPIRES);
            
            LoginResponseDto dto2 = new LoginResponseDto(
                    DIFFERENT_TOKEN, DIFFERENT_EMAIL, DIFFERENT_NAME, DIFFERENT_ROLE, DIFFERENT_EXPIRES);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private LoginResponseDto createTestDto() {
        return LoginResponseDto.builder()
                .accessToken(TEST_TOKEN)
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .role(TEST_ROLE)
                .expiresIn(TEST_EXPIRES)
                .build();
    }
    
    private void assertAllFieldsMatch(LoginResponseDto dto) {
        assertEquals(TEST_TOKEN, dto.getAccessToken());
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_ROLE, dto.getRole());
        assertEquals(TEST_EXPIRES, dto.getExpiresIn());
    }
}