package id.ac.ui.cs.advprog.beauthentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PasswordChangeDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_shouldHaveNoViolations() {
        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("confirmPassword");

        Set<ConstraintViolation<PasswordChangeDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenCurrentPasswordBlank_shouldHaveViolation() {
        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("confirmPassword");

        Set<ConstraintViolation<PasswordChangeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Current password is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenNewPasswordBlank_shouldHaveViolation() {
        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("");
        dto.setConfirmPassword("confirmPassword");

        Set<ConstraintViolation<PasswordChangeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("New password is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenConfirmPasswordBlank_shouldHaveViolation() {
        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("currentPassword");
        dto.setNewPassword("newPassword");
        dto.setConfirmPassword("");

        Set<ConstraintViolation<PasswordChangeDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Confirm password is required", violations.iterator().next().getMessage());
    }

    @Test
    void testBuilderAndNoArgsConstructor() {
        PasswordChangeDto builderDto = PasswordChangeDto.builder()
                .currentPassword("current")
                .newPassword("new")
                .confirmPassword("confirm")
                .build();

        assertEquals("current", builderDto.getCurrentPassword());
        assertEquals("new", builderDto.getNewPassword());
        assertEquals("confirm", builderDto.getConfirmPassword());

        PasswordChangeDto noArgsDto = new PasswordChangeDto();
        assertNull(noArgsDto.getCurrentPassword());
        assertNull(noArgsDto.getNewPassword());
        assertNull(noArgsDto.getConfirmPassword());
    }

    @Test
    void testAllArgsConstructor() {
        PasswordChangeDto dto = new PasswordChangeDto("current", "new", "confirm");
        
        assertEquals("current", dto.getCurrentPassword());
        assertEquals("new", dto.getNewPassword());
        assertEquals("confirm", dto.getConfirmPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        PasswordChangeDto dto1 = new PasswordChangeDto("current", "new", "confirm");
        PasswordChangeDto dto2 = new PasswordChangeDto("current", "new", "confirm");
        PasswordChangeDto dto3 = new PasswordChangeDto("different", "new", "confirm");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        PasswordChangeDto dto = new PasswordChangeDto("current", "new", "confirm");
        String toString = dto.toString();
        
        assertTrue(toString.contains("currentPassword=current"));
        assertTrue(toString.contains("newPassword=new"));
        assertTrue(toString.contains("confirmPassword=confirm"));
    }
}