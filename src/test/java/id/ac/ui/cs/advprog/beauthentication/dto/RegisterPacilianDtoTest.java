package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterPacilianDtoTest {

    @Test
    void builderCreatesRegisterPacilianDto() {
        RegisterPacilianDto dto = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();
        
        assertEquals("patient@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Patient Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Patient Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Some medical history", dto.getMedicalHistory());
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
                "patient@example.com",
                "password",
                "Patient Name",
                "1234567890123456",
                "Patient Address",
                "1234567890",
                "Some medical history");
        
        assertEquals("patient@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Patient Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Patient Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Some medical history", dto.getMedicalHistory());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        RegisterPacilianDto dto = new RegisterPacilianDto();
        
        dto.setEmail("patient@example.com");
        dto.setPassword("password");
        dto.setName("Patient Name");
        dto.setNik("1234567890123456");
        dto.setAddress("Patient Address");
        dto.setPhoneNumber("1234567890");
        dto.setMedicalHistory("Some medical history");
        
        assertEquals("patient@example.com", dto.getEmail());
        assertEquals("password", dto.getPassword());
        assertEquals("Patient Name", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Patient Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Some medical history", dto.getMedicalHistory());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        RegisterPacilianDto dto1 = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();
        
        RegisterPacilianDto dto2 = RegisterPacilianDto.builder()
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .build();
        
        RegisterPacilianDto dto3 = RegisterPacilianDto.builder()
                .email("different@example.com")
                .password("different")
                .name("Different Name")
                .nik("6543210987654321")
                .address("Different Address")
                .phoneNumber("0987654321")
                .medicalHistory("Different medical history")
                .build();
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}