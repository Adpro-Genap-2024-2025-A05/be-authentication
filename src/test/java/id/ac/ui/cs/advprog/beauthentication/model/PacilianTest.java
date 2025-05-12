package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacilianTest {

    @Nested
    class BuilderTests {
        @Test
        void builderCreatesPacilianWithAllProperties() {
            Pacilian pacilian = createFullPacilian();

            assertEquals("pacilian-id", pacilian.getId());
            assertEquals("patient@example.com", pacilian.getEmail());
            assertEquals("password", pacilian.getPassword());
            assertEquals("Patient Name", pacilian.getName());
            assertEquals("1234567890123456", pacilian.getNik());
            assertEquals("Patient Address", pacilian.getAddress());
            assertEquals("1234567890", pacilian.getPhoneNumber());
            assertEquals("Some medical history", pacilian.getMedicalHistory());
            assertEquals(Role.PACILIAN, pacilian.getRole());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void medicalHistoryGetterAndSetterWorkCorrectly() {
            Pacilian pacilian = new Pacilian();

            pacilian.setMedicalHistory("Updated medical history");

            assertEquals("Updated medical history", pacilian.getMedicalHistory());
        }
    }

    @Nested
    class InheritanceTests {
        @Test
        void pacilianIsInstanceOfUser() {
            Pacilian pacilian = new Pacilian();

            assertInstanceOf(User.class, pacilian);
        }

        @Test
        void onCreateSetsPacilianRoleIfNull() {
            Pacilian pacilian = new Pacilian();

            pacilian.onCreate();

            assertEquals(Role.PACILIAN, pacilian.getRole());
        }

        @Test
        void onCreateDoesNotChangeExistingRole() {
            Pacilian pacilian = new Pacilian();
            Role initialRole = Role.PACILIAN;
            pacilian.setRole(initialRole);

            pacilian.onCreate();

            assertEquals(initialRole, pacilian.getRole());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForSameId() {
            Pacilian pacilian1 = Pacilian.builder().id("pacilian-id").build();
            Pacilian pacilian2 = Pacilian.builder().id("pacilian-id").build();

            assertEquals(pacilian1, pacilian2);
        }

        @Test
        void equalsReturnsFalseForDifferentId() {
            Pacilian pacilian1 = Pacilian.builder().id("pacilian-id").build();
            Pacilian pacilian2 = Pacilian.builder().id("different-id").build();

            assertNotEquals(pacilian1, pacilian2);
        }

        @Test
        void hashCodeReturnsSameValueForEqualObjects() {
            Pacilian pacilian1 = Pacilian.builder()
                    .id("pacilian-id")
                    .medicalHistory("Some medical history")
                    .build();

            Pacilian pacilian2 = Pacilian.builder()
                    .id("pacilian-id")
                    .medicalHistory("Some medical history")
                    .build();

            assertEquals(pacilian1.hashCode(), pacilian2.hashCode());
        }
    }

    private Pacilian createFullPacilian() {
        return Pacilian.builder()
                .id("pacilian-id")
                .email("patient@example.com")
                .password("password")
                .name("Patient Name")
                .nik("1234567890123456")
                .address("Patient Address")
                .phoneNumber("1234567890")
                .medicalHistory("Some medical history")
                .role(Role.PACILIAN)
                .build();
    }
}