package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality; 
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileDtoTest {

    @Test
    void testBuilderAndNoArgsConstructor() {
        UserProfileDto builderDto = UserProfileDto.builder()
                .id("test-id")
                .email("test@example.com")
                .name("Test User")
                .nik("1234567890123456")
                .address("Test Address")
                .phoneNumber("1234567890")
                .role(Role.PACILIAN)
                .medicalHistory("Test medical history")
                .speciality(Speciality.DOKTER_UMUM) 
                .workAddress("Test work address")
                .build();

        assertEquals("test-id", builderDto.getId());
        assertEquals("test@example.com", builderDto.getEmail());
        assertEquals("Test User", builderDto.getName());
        assertEquals("1234567890123456", builderDto.getNik());
        assertEquals("Test Address", builderDto.getAddress());
        assertEquals("1234567890", builderDto.getPhoneNumber());
        assertEquals(Role.PACILIAN, builderDto.getRole());
        assertEquals("Test medical history", builderDto.getMedicalHistory());
        assertEquals(Speciality.DOKTER_UMUM, builderDto.getSpeciality()); 
        assertEquals("Test work address", builderDto.getWorkAddress());

        UserProfileDto noArgsDto = new UserProfileDto();
        assertNull(noArgsDto.getId());
        assertNull(noArgsDto.getEmail());
        assertNull(noArgsDto.getName());
        assertNull(noArgsDto.getNik());
        assertNull(noArgsDto.getAddress());
        assertNull(noArgsDto.getPhoneNumber());
        assertNull(noArgsDto.getRole());
        assertNull(noArgsDto.getMedicalHistory());
        assertNull(noArgsDto.getSpeciality());
        assertNull(noArgsDto.getWorkAddress());
    }

    @Test
    void testAllArgsConstructor() {
        UserProfileDto dto = new UserProfileDto(
                "test-id",
                "test@example.com",
                "Test User",
                "1234567890123456",
                "Test Address",
                "1234567890",
                Role.PACILIAN,
                "Test medical history",
                Speciality.SPESIALIS_ANAK, 
                "Test work address"
        );
        
        assertEquals("test-id", dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Test User", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Test Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals("Test medical history", dto.getMedicalHistory());
        assertEquals(Speciality.SPESIALIS_ANAK, dto.getSpeciality()); 
        assertEquals("Test work address", dto.getWorkAddress());
    }

    @Test
    void testSettersAndGetters() {
        UserProfileDto dto = new UserProfileDto();
        
        dto.setId("test-id");
        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setNik("1234567890123456");
        dto.setAddress("Test Address");
        dto.setPhoneNumber("1234567890");
        dto.setRole(Role.PACILIAN);
        dto.setMedicalHistory("Test medical history");
        dto.setSpeciality(Speciality.SPESIALIS_KULIT); 
        dto.setWorkAddress("Test work address");
        
        assertEquals("test-id", dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Test User", dto.getName());
        assertEquals("1234567890123456", dto.getNik());
        assertEquals("Test Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals(Role.PACILIAN, dto.getRole());
        assertEquals("Test medical history", dto.getMedicalHistory());
        assertEquals(Speciality.SPESIALIS_KULIT, dto.getSpeciality()); 
        assertEquals("Test work address", dto.getWorkAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        UserProfileDto dto1 = new UserProfileDto(
                "test-id",
                "test@example.com",
                "Test User",
                "1234567890123456",
                "Test Address",
                "1234567890",
                Role.PACILIAN,
                "Test medical history",
                Speciality.DOKTER_UMUM, 
                "Test work address"
        );
        
        UserProfileDto dto2 = new UserProfileDto(
                "test-id",
                "test@example.com",
                "Test User",
                "1234567890123456",
                "Test Address",
                "1234567890",
                Role.PACILIAN,
                "Test medical history",
                Speciality.DOKTER_UMUM, 
                "Test work address"
        );
        
        UserProfileDto dto3 = new UserProfileDto(
                "different-id",
                "test@example.com",
                "Test User",
                "1234567890123456",
                "Test Address",
                "1234567890",
                Role.PACILIAN,
                "Test medical history",
                Speciality.SPESIALIS_ANAK, 
                "Test work address"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        UserProfileDto dto = new UserProfileDto(
                "test-id",
                "test@example.com",
                "Test User",
                "1234567890123456",
                "Test Address",
                "1234567890",
                Role.PACILIAN,
                "Test medical history",
                Speciality.DOKTER_UMUM, 
                "Test work address"
        );
        
        String toString = dto.toString();
        
        assertTrue(toString.contains("id=test-id"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("name=Test User"));
        assertTrue(toString.contains("nik=1234567890123456"));
        assertTrue(toString.contains("address=Test Address"));
        assertTrue(toString.contains("phoneNumber=1234567890"));
        assertTrue(toString.contains("role=PACILIAN"));
        assertTrue(toString.contains("medicalHistory=Test medical history"));
        assertTrue(toString.contains("speciality=DOKTER_UMUM")); 
        assertTrue(toString.contains("workAddress=Test work address"));
    }
}