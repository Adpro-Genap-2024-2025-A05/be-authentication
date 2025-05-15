package id.ac.ui.cs.advprog.beauthentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProfileDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_shouldHaveNoViolations() {
        UpdateProfileDto dto = new UpdateProfileDto();
        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setAddress("Test Address");
        dto.setPhoneNumber("1234567890");
        dto.setMedicalHistory("Test medical history");
        dto.setSpeciality("Test speciality");
        dto.setWorkAddress("Test work address");

        Set<ConstraintViolation<UpdateProfileDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEmailInvalid_shouldHaveViolation() {
        UpdateProfileDto dto = new UpdateProfileDto();
        dto.setEmail("invalid-email");
        dto.setName("Test User");
        dto.setAddress("Test Address");
        dto.setPhoneNumber("1234567890");

        Set<ConstraintViolation<UpdateProfileDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void whenPhoneNumberInvalid_shouldHaveViolation() {
        UpdateProfileDto dto = new UpdateProfileDto();
        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setAddress("Test Address");
        dto.setPhoneNumber("123"); 

        Set<ConstraintViolation<UpdateProfileDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number must be 10-13 digits", violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsNull_shouldHaveNoViolations() {
        UpdateProfileDto dto = new UpdateProfileDto();

        Set<ConstraintViolation<UpdateProfileDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testBuilderAndNoArgsConstructor() {
        UpdateProfileDto builderDto = UpdateProfileDto.builder()
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phoneNumber("1234567890")
                .medicalHistory("Test medical history")
                .speciality("Test speciality")
                .workAddress("Test work address")
                .build();

        assertEquals("test@example.com", builderDto.getEmail());
        assertEquals("Test User", builderDto.getName());
        assertEquals("Test Address", builderDto.getAddress());
        assertEquals("1234567890", builderDto.getPhoneNumber());
        assertEquals("Test medical history", builderDto.getMedicalHistory());
        assertEquals("Test speciality", builderDto.getSpeciality());
        assertEquals("Test work address", builderDto.getWorkAddress());

        UpdateProfileDto noArgsDto = new UpdateProfileDto();
        assertNull(noArgsDto.getEmail());
        assertNull(noArgsDto.getName());
        assertNull(noArgsDto.getAddress());
        assertNull(noArgsDto.getPhoneNumber());
        assertNull(noArgsDto.getMedicalHistory());
        assertNull(noArgsDto.getSpeciality());
        assertNull(noArgsDto.getWorkAddress());
    }

    @Test
    void testAllArgsConstructor() {
        UpdateProfileDto dto = new UpdateProfileDto(
                "test@example.com",
                "Test User",
                "Test Address",
                "1234567890",
                "Test medical history",
                "Test speciality",
                "Test work address"
        );
        
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Test User", dto.getName());
        assertEquals("Test Address", dto.getAddress());
        assertEquals("1234567890", dto.getPhoneNumber());
        assertEquals("Test medical history", dto.getMedicalHistory());
        assertEquals("Test speciality", dto.getSpeciality());
        assertEquals("Test work address", dto.getWorkAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateProfileDto dto1 = new UpdateProfileDto(
                "test@example.com",
                "Test User",
                "Test Address",
                "1234567890",
                "Test medical history",
                "Test speciality",
                "Test work address"
        );
        
        UpdateProfileDto dto2 = new UpdateProfileDto(
                "test@example.com",
                "Test User",
                "Test Address",
                "1234567890",
                "Test medical history",
                "Test speciality",
                "Test work address"
        );
        
        UpdateProfileDto dto3 = new UpdateProfileDto(
                "different@example.com",
                "Test User",
                "Test Address",
                "1234567890",
                "Test medical history",
                "Test speciality",
                "Test work address"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        UpdateProfileDto dto = new UpdateProfileDto(
                "test@example.com",
                "Test User",
                "Test Address",
                "1234567890",
                "Test medical history",
                "Test speciality",
                "Test work address"
        );
        
        String toString = dto.toString();
        
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("name=Test User"));
        assertTrue(toString.contains("address=Test Address"));
        assertTrue(toString.contains("phoneNumber=1234567890"));
        assertTrue(toString.contains("medicalHistory=Test medical history"));
        assertTrue(toString.contains("speciality=Test speciality"));
        assertTrue(toString.contains("workAddress=Test work address"));
    }
}