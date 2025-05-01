package id.ac.ui.cs.advprog.beauthentication.enums;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Nested
    class GetValueTests {
        @Test
        void getValueReturnsCorrectStringValue() {
            assertEquals("PACILIAN", Role.PACILIAN.getValue());
            assertEquals("CAREGIVER", Role.CAREGIVER.getValue());
        }
    }

    @Nested
    class ContainsMethodTests {
        @Test
        void containsValidRoles_ReturnsTrue() {
            assertTrue(Role.contains("PACILIAN"));
            assertTrue(Role.contains("CAREGIVER"));
        }

        @Test
        void containsInvalidStringRoles_ReturnsFalse() {
            assertFalse(Role.contains("ADMIN"));
            assertFalse(Role.contains("USER"));
            assertFalse(Role.contains(""));
        }
        
        @Test
        void containsNullParam_ReturnsFalse() {
            assertFalse(Role.contains(null));
        }
    }
}