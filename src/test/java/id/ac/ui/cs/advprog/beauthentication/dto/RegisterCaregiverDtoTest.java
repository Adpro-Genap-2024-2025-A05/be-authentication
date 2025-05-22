package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCaregiverDtoTest {

    private static final String TEST_EMAIL = "caregiver@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NAME = "Caregiver Name";
    private static final String TEST_NIK = "1234567890123456";
    private static final String TEST_WORK_ADDRESS = "Work Address";
    private static final String TEST_PHONE = "1234567890";
    private static final Speciality TEST_SPECIALITY = Speciality.DOKTER_UMUM;

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesRegisterCaregiverDto() {
            RegisterCaregiverDto dto = RegisterCaregiverDto.builder()
                    .email(TEST_EMAIL)
                    .password(TEST_PASSWORD)
                    .name(TEST_NAME)
                    .nik(TEST_NIK)
                    .workAddress(TEST_WORK_ADDRESS)
                    .phoneNumber(TEST_PHONE)
                    .speciality(TEST_SPECIALITY)
                    .build();
            
            assertAllFieldsMatch(dto);
        }

        @Test
        void noArgsConstructorCreatesEmptyRegisterCaregiverDto() {
            RegisterCaregiverDto dto = new RegisterCaregiverDto();
            
            assertNull(dto.getEmail());
            assertNull(dto.getPassword());
            assertNull(dto.getName());
            assertNull(dto.getNik());
            assertNull(dto.getWorkAddress());
            assertNull(dto.getPhoneNumber());
            assertNull(dto.getSpeciality());
        }

        @Test
        void allArgsConstructorCreatesRegisterCaregiverDto() {
            RegisterCaregiverDto dto = new RegisterCaregiverDto(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_NAME,
                    TEST_NIK,
                    TEST_WORK_ADDRESS,
                    TEST_PHONE,
                    TEST_SPECIALITY);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            RegisterCaregiverDto dto = new RegisterCaregiverDto();
            
            dto.setEmail(TEST_EMAIL);
            dto.setPassword(TEST_PASSWORD);
            dto.setName(TEST_NAME);
            dto.setNik(TEST_NIK);
            dto.setWorkAddress(TEST_WORK_ADDRESS);
            dto.setPhoneNumber(TEST_PHONE);
            dto.setSpeciality(TEST_SPECIALITY);
            
            assertAllFieldsMatch(dto);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            RegisterCaregiverDto dto1 = createTestDto();
            RegisterCaregiverDto dto2 = createTestDto();
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            RegisterCaregiverDto dto1 = createTestDto();
            
            RegisterCaregiverDto dto2 = RegisterCaregiverDto.builder()
                    .email("different@example.com")
                    .password("different")
                    .name("Different Name")
                    .nik("6543210987654321")
                    .workAddress("Different Address")
                    .phoneNumber("0987654321")
                    .speciality(Speciality.SPESIALIS_ANAK)
                    .build();
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private RegisterCaregiverDto createTestDto() {
        return RegisterCaregiverDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .workAddress(TEST_WORK_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .speciality(TEST_SPECIALITY)
                .build();
    }
    
    private void assertAllFieldsMatch(RegisterCaregiverDto dto) {
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_PASSWORD, dto.getPassword());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_NIK, dto.getNik());
        assertEquals(TEST_WORK_ADDRESS, dto.getWorkAddress());
        assertEquals(TEST_PHONE, dto.getPhoneNumber());
        assertEquals(TEST_SPECIALITY, dto.getSpeciality());
    }
}