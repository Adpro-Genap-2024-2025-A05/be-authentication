package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterResponseDtoTest {

    private static final String TEST_ID = "user-id";
    private static final Role TEST_ROLE = Role.PACILIAN;
    private static final String TEST_MESSAGE = "Registration successful";
    private static final String DIFFERENT_ID = "different-id";
    private static final Role DIFFERENT_ROLE = Role.CAREGIVER;
    private static final String DIFFERENT_MESSAGE = "Different message";

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesRegisterResponseDto() {
            RegisterResponseDto dto = createTestDto();
            
            assertAllFieldsMatch(dto);
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
            RegisterResponseDto dto = new RegisterResponseDto(TEST_ID, TEST_ROLE, TEST_MESSAGE);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            RegisterResponseDto dto = new RegisterResponseDto();
            
            dto.setId(TEST_ID);
            dto.setRole(TEST_ROLE);
            dto.setMessage(TEST_MESSAGE);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            RegisterResponseDto dto1 = new RegisterResponseDto(TEST_ID, TEST_ROLE, TEST_MESSAGE);
            RegisterResponseDto dto2 = new RegisterResponseDto(TEST_ID, TEST_ROLE, TEST_MESSAGE);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            RegisterResponseDto dto1 = new RegisterResponseDto(TEST_ID, TEST_ROLE, TEST_MESSAGE);
            RegisterResponseDto dto2 = new RegisterResponseDto(DIFFERENT_ID, DIFFERENT_ROLE, DIFFERENT_MESSAGE);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private RegisterResponseDto createTestDto() {
        return RegisterResponseDto.builder()
                .id(TEST_ID)
                .role(TEST_ROLE)
                .message(TEST_MESSAGE)
                .build();
    }
    
    private void assertAllFieldsMatch(RegisterResponseDto dto) {
        assertEquals(TEST_ID, dto.getId());
        assertEquals(TEST_ROLE, dto.getRole());
        assertEquals(TEST_MESSAGE, dto.getMessage());
    }
}