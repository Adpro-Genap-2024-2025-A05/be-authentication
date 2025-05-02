package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenVerificationResponseDtoTest {

    private static final boolean TEST_VALID = true;
    private static final String TEST_USER_ID = "user-id";
    private static final String TEST_EMAIL = "user@example.com";
    private static final Role TEST_ROLE = Role.PACILIAN;
    private static final Long TEST_EXPIRES = 3600L;
    private static final boolean DIFFERENT_VALID = false;
    private static final String DIFFERENT_USER_ID = "different-id";
    private static final String DIFFERENT_EMAIL = "different@example.com";
    private static final Role DIFFERENT_ROLE = Role.CAREGIVER;
    private static final Long DIFFERENT_EXPIRES = 7200L;

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesTokenVerificationResponseDto() {
            TokenVerificationResponseDto dto = createTestDto();
            
            assertAllFieldsMatch(dto);
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
                    TEST_VALID, TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_EXPIRES);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            TokenVerificationResponseDto dto = new TokenVerificationResponseDto();
            
            dto.setValid(TEST_VALID);
            dto.setUserId(TEST_USER_ID);
            dto.setEmail(TEST_EMAIL);
            dto.setRole(TEST_ROLE);
            dto.setExpiresIn(TEST_EXPIRES);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            TokenVerificationResponseDto dto1 = new TokenVerificationResponseDto(
                    TEST_VALID, TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_EXPIRES);
            
            TokenVerificationResponseDto dto2 = new TokenVerificationResponseDto(
                    TEST_VALID, TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_EXPIRES);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            TokenVerificationResponseDto dto1 = new TokenVerificationResponseDto(
                    TEST_VALID, TEST_USER_ID, TEST_EMAIL, TEST_ROLE, TEST_EXPIRES);
            
            TokenVerificationResponseDto dto2 = new TokenVerificationResponseDto(
                    DIFFERENT_VALID, DIFFERENT_USER_ID, DIFFERENT_EMAIL, DIFFERENT_ROLE, DIFFERENT_EXPIRES);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private TokenVerificationResponseDto createTestDto() {
        return TokenVerificationResponseDto.builder()
                .valid(TEST_VALID)
                .userId(TEST_USER_ID)
                .email(TEST_EMAIL)
                .role(TEST_ROLE)
                .expiresIn(TEST_EXPIRES)
                .build();
    }
    
    private void assertAllFieldsMatch(TokenVerificationResponseDto dto) {
        assertTrue(dto.isValid());
        assertEquals(TEST_USER_ID, dto.getUserId());
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_ROLE, dto.getRole());
        assertEquals(TEST_EXPIRES, dto.getExpiresIn());
    }
}