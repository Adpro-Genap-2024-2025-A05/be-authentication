package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterPacilianDtoTest {

    private static final String TEST_EMAIL = "patient@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NAME = "Patient Name";
    private static final String TEST_NIK = "1234567890123456";
    private static final String TEST_ADDRESS = "Patient Address";
    private static final String TEST_PHONE = "1234567890";
    private static final String TEST_MEDICAL_HISTORY = "Some medical history";

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesRegisterPacilianDto() {
            RegisterPacilianDto dto = createTestDto();
            
            assertAllFieldsMatch(dto);
        }

        @Test
        void noArgsConstructorCreatesEmptyRegisterPacilianDto() {
            RegisterPacilianDto dto = new RegisterPacilianDto();
            
            assertNull(dto.getEmail());
            assertNull(dto.getPassword());
            assertNull(dto.getName());
            assertNull(dto.getNik());
            assertNull(dto.getAddress());
            assertNull(dto.getPhoneNumber());
            assertNull(dto.getMedicalHistory());
        }

        @Test
        void allArgsConstructorCreatesRegisterPacilianDto() {
            RegisterPacilianDto dto = new RegisterPacilianDto(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_NAME,
                    TEST_NIK,
                    TEST_ADDRESS,
                    TEST_PHONE,
                    TEST_MEDICAL_HISTORY);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            RegisterPacilianDto dto = new RegisterPacilianDto();
            
            dto.setEmail(TEST_EMAIL);
            dto.setPassword(TEST_PASSWORD);
            dto.setName(TEST_NAME);
            dto.setNik(TEST_NIK);
            dto.setAddress(TEST_ADDRESS);
            dto.setPhoneNumber(TEST_PHONE);
            dto.setMedicalHistory(TEST_MEDICAL_HISTORY);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            RegisterPacilianDto dto1 = createTestDto();
            RegisterPacilianDto dto2 = createTestDto();
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            RegisterPacilianDto dto1 = createTestDto();
            
            RegisterPacilianDto dto2 = RegisterPacilianDto.builder()
                    .email("different@example.com")
                    .password("different")
                    .name("Different Name")
                    .nik("6543210987654321")
                    .address("Different Address")
                    .phoneNumber("0987654321")
                    .medicalHistory("Different medical history")
                    .build();
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }

    private RegisterPacilianDto createTestDto() {
        return RegisterPacilianDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .address(TEST_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .medicalHistory(TEST_MEDICAL_HISTORY)
                .build();
    }
    
    private void assertAllFieldsMatch(RegisterPacilianDto dto) {
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_PASSWORD, dto.getPassword());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_NIK, dto.getNik());
        assertEquals(TEST_ADDRESS, dto.getAddress());
        assertEquals(TEST_PHONE, dto.getPhoneNumber());
        assertEquals(TEST_MEDICAL_HISTORY, dto.getMedicalHistory());
    }
}