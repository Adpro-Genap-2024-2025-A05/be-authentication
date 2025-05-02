package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCaregiverDtoTest {

    private static final String TEST_EMAIL = "caregiver@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NAME = "Caregiver Name";
    private static final String TEST_NIK = "1234567890123456";
    private static final String TEST_WORK_ADDRESS = "Work Address";
    private static final String TEST_PHONE = "1234567890";
    private static final String TEST_SPECIALITY = "Cardiology";

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesRegisterCaregiverDto() {
            List<WorkingScheduleDto> scheduleDtos = createTestSchedules();
            
            RegisterCaregiverDto dto = RegisterCaregiverDto.builder()
                    .email(TEST_EMAIL)
                    .password(TEST_PASSWORD)
                    .name(TEST_NAME)
                    .nik(TEST_NIK)
                    .workAddress(TEST_WORK_ADDRESS)
                    .phoneNumber(TEST_PHONE)
                    .speciality(TEST_SPECIALITY)
                    .workingSchedules(scheduleDtos)
                    .build();
            
            assertAllFieldsMatch(dto, scheduleDtos);
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
            assertNull(dto.getWorkingSchedules());
        }

        @Test
        void allArgsConstructorCreatesRegisterCaregiverDto() {
            List<WorkingScheduleDto> scheduleDtos = createTestSchedules();
            
            RegisterCaregiverDto dto = new RegisterCaregiverDto(
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_NAME,
                    TEST_NIK,
                    TEST_WORK_ADDRESS,
                    TEST_PHONE,
                    TEST_SPECIALITY,
                    scheduleDtos);
            
            assertAllFieldsMatch(dto, scheduleDtos);
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            RegisterCaregiverDto dto = new RegisterCaregiverDto();
            List<WorkingScheduleDto> scheduleDtos = createTestSchedules();
            
            dto.setEmail(TEST_EMAIL);
            dto.setPassword(TEST_PASSWORD);
            dto.setName(TEST_NAME);
            dto.setNik(TEST_NIK);
            dto.setWorkAddress(TEST_WORK_ADDRESS);
            dto.setPhoneNumber(TEST_PHONE);
            dto.setSpeciality(TEST_SPECIALITY);
            dto.setWorkingSchedules(scheduleDtos);
            
            assertAllFieldsMatch(dto, scheduleDtos);
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            List<WorkingScheduleDto> scheduleDtos1 = createTestSchedules();
            List<WorkingScheduleDto> scheduleDtos2 = createTestSchedules();
            
            RegisterCaregiverDto dto1 = createTestDto(scheduleDtos1);
            RegisterCaregiverDto dto2 = createTestDto(scheduleDtos2);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentObjects() {
            List<WorkingScheduleDto> scheduleDtos = createTestSchedules();
            List<WorkingScheduleDto> differentSchedules = createDifferentSchedules();
            
            RegisterCaregiverDto dto1 = createTestDto(scheduleDtos);
            
            RegisterCaregiverDto dto2 = RegisterCaregiverDto.builder()
                    .email("different@example.com")
                    .password("different")
                    .name("Different Name")
                    .nik("6543210987654321")
                    .workAddress("Different Address")
                    .phoneNumber("0987654321")
                    .speciality("Neurology")
                    .workingSchedules(differentSchedules)
                    .build();
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }

    private List<WorkingScheduleDto> createTestSchedules() {
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);
        return scheduleDtos;
    }
    
    private List<WorkingScheduleDto> createDifferentSchedules() {
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);
        return scheduleDtos;
    }
    
    private RegisterCaregiverDto createTestDto(List<WorkingScheduleDto> scheduleDtos) {
        return RegisterCaregiverDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name(TEST_NAME)
                .nik(TEST_NIK)
                .workAddress(TEST_WORK_ADDRESS)
                .phoneNumber(TEST_PHONE)
                .speciality(TEST_SPECIALITY)
                .workingSchedules(scheduleDtos)
                .build();
    }
    
    private void assertAllFieldsMatch(RegisterCaregiverDto dto, List<WorkingScheduleDto> scheduleDtos) {
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals(TEST_PASSWORD, dto.getPassword());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_NIK, dto.getNik());
        assertEquals(TEST_WORK_ADDRESS, dto.getWorkAddress());
        assertEquals(TEST_PHONE, dto.getPhoneNumber());
        assertEquals(TEST_SPECIALITY, dto.getSpeciality());
        assertEquals(scheduleDtos, dto.getWorkingSchedules());
    }
}