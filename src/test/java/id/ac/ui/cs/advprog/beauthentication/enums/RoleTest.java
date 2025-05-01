package id.ac.ui.cs.advprog.beauthentication.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void getValueReturnsCorrectStringValue() {
        assertEquals("PACILIAN", Role.PACILIAN.getValue());
        assertEquals("CAREGIVER", Role.CAREGIVER.getValue());
    }

    @Test
    void containsValidRoles_ReturnsTrue() {
        assertTrue(Role.contains("PACILIAN"));
        assertTrue(Role.contains("CAREGIVER"));
    }

    @Test
    void containsInvalidRolesReturnsFalse() {
        assertFalse(Role.contains("ADMIN"));
        assertFalse(Role.contains("USER"));
        assertFalse(Role.contains(""));
        assertFalse(Role.contains(null));
    }
}