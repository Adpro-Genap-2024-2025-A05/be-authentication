package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverTest {

    @Nested
    class BuilderTests {
        @Test
        void builderCreatesFullCaregiver() {
            Caregiver caregiver = createFullCaregiver();

            assertEquals("caregiver-id", caregiver.getId());
            assertEquals("caregiver@example.com", caregiver.getEmail());
            assertEquals("password", caregiver.getPassword());
            assertEquals("Caregiver Name", caregiver.getName());
            assertEquals("1234567890123456", caregiver.getNik());
            assertEquals("Home Address", caregiver.getAddress());
            assertEquals("Work Address", caregiver.getWorkAddress());
            assertEquals("1234567890", caregiver.getPhoneNumber());
            assertEquals("Cardiology", caregiver.getSpeciality());
            assertEquals(Role.CAREGIVER, caregiver.getRole());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void specialityGetterAndSetterWorkCorrectly() {
            Caregiver caregiver = new Caregiver();

            caregiver.setSpeciality("Neurology");

            assertEquals("Neurology", caregiver.getSpeciality());
        }

        @Test
        void workAddressGetterAndSetterWorkCorrectly() {
            Caregiver caregiver = new Caregiver();

            caregiver.setWorkAddress("New Work Address");

            assertEquals("New Work Address", caregiver.getWorkAddress());
        }
    }

    @Nested
    class InheritanceTests {
        @Test
        void caregiverIsInstanceOfUser() {
            Caregiver caregiver = new Caregiver();

            assertInstanceOf(User.class, caregiver);
        }

        @Test
        void onCreateSetsCaregiverRoleIfNull() {
            Caregiver caregiver = new Caregiver();

            caregiver.onCreate();

            assertEquals(Role.CAREGIVER, caregiver.getRole());
        }

        @Test
        void onCreateDoesNotChangeExistingRole() {
            Caregiver caregiver = new Caregiver();
            Role initialRole = Role.CAREGIVER;
            caregiver.setRole(initialRole);

            caregiver.onCreate();

            assertEquals(initialRole, caregiver.getRole());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForSameId() {
            Caregiver caregiver1 = Caregiver.builder().id("caregiver-id").build();
            Caregiver caregiver2 = Caregiver.builder().id("caregiver-id").build();

            assertEquals(caregiver1, caregiver2);
        }

        @Test
        void equalsReturnsFalseForDifferentId() {
            Caregiver caregiver1 = Caregiver.builder().id("caregiver-id").build();
            Caregiver caregiver2 = Caregiver.builder().id("different-id").build();

            assertNotEquals(caregiver1, caregiver2);
        }

        @Test
        void hashCodeReturnsSameValueForEqualObjects() {
            Caregiver caregiver1 = Caregiver.builder()
                    .id("caregiver-id")
                    .speciality("Cardiology")
                    .build();

            Caregiver caregiver2 = Caregiver.builder()
                    .id("caregiver-id")
                    .speciality("Cardiology")
                    .build();

            assertEquals(caregiver1.hashCode(), caregiver2.hashCode());
        }
    }

    private Caregiver createFullCaregiver() {
        return Caregiver.builder()
                .id("caregiver-id")
                .email("caregiver@example.com")
                .password("password")
                .name("Caregiver Name")
                .nik("1234567890123456")
                .address("Home Address")
                .workAddress("Work Address")
                .phoneNumber("1234567890")
                .speciality("Cardiology")
                .role(Role.CAREGIVER)
                .build();
    }
}