package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCaregiverDtoTest {

    @Test
    void builderCreatesRegisterCaregiverDto() {
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);
        
        RegisterCaregiverDto dto = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(scheduleDtos)
                .build();
        
        assertEquals("caregiver@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Caregiver Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Work Address", dto.getWorkAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Cardiology", dto.getSpeciality());
        assertEquals(scheduleDtos, dto.getWorkingSchedules());
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
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);
        
        RegisterCaregiverDto dto = new RegisterCaregiverDto(
                "caregiver@example.com",
                "password",
                "Caregiver Name",
                "1234567890123456",
                "Work Address",
                "1234567890",
                "Cardiology",
                scheduleDtos);
        
        assertEquals("caregiver@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Caregiver Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Work Address", dto.getWorkAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Cardiology", dto.getSpeciality());
        assertEquals(scheduleDtos, dto.getWorkingSchedules());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        RegisterCaregiverDto dto = new RegisterCaregiverDto();
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleDtos.add(scheduleDto);
        
        dto.setEmail("caregiver@example.com");
        dto.setPassword("password");
        dto.setName("Caregiver Name");
        dto.setNik("1234567890123456");
        dto.setWorkAddress("Work Address");
        dto.setPhoneNumber("1234567890");
        dto.setSpeciality("Cardiology");
        dto.setWorkingSchedules(scheduleDtos);
        
        assertEquals("caregiver@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Caregiver Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Work Address", dto.getWorkAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Cardiology", dto.getSpeciality());
        assertEquals(scheduleDtos, dto.getWorkingSchedules());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        WorkingScheduleDto scheduleDto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        List<WorkingScheduleDto> scheduleDtos1 = new ArrayList<>();
        scheduleDtos1.add(scheduleDto);
        
        List<WorkingScheduleDto> scheduleDtos2 = new ArrayList<>();
        scheduleDtos2.add(scheduleDto);
        
        List<WorkingScheduleDto> differentSchedules = new ArrayList<>();
        differentSchedules.add(WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .build());
        
        RegisterCaregiverDto dto1 = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(scheduleDtos1)
                .build();
        
        RegisterCaregiverDto dto2 = RegisterCaregiverDto.builder()
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .workingSchedules(scheduleDtos2)
                .build();
        
        RegisterCaregiverDto dto3 = RegisterCaregiverDto.builder()
                .email("different@example.com")
                .password("different")
                .name("Different Name")
                .nik("6543210987654321")
                .workAddress("Different Address")
                .phoneNumber("0987654321")
                .speciality("Neurology")
                .workingSchedules(differentSchedules)
                .build();
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}