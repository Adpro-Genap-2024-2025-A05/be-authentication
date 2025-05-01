package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PacilianTest {

    @Test
    void builderCreatesPacilian() {
        Pacilian pacilian = Pacilian.builder()
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

    @Test
    void gettersAndSettersWorkCorrectly() {
        Pacilian pacilian = new Pacilian();
        pacilian.setMedicalHistory("Updated medical history");
        assertEquals("Updated medical history", pacilian.getMedicalHistory());
    }

    @Test
    void inheritanceExtendedFromUser() {
        Pacilian pacilian = new Pacilian();
        assertInstanceOf(User.class, pacilian);
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        Pacilian pacilian1 = Pacilian.builder()
                .id("pacilian-id")
                .email("patient@example.com")
                .medicalHistory("Some medical history")
                .build();
        
        Pacilian pacilian2 = Pacilian.builder()
                .id("pacilian-id")
                .email("patient@example.com")
                .medicalHistory("Some medical history")
                .build();
        
        Pacilian pacilian3 = Pacilian.builder()
                .id("different-id")
                .email("different@example.com")
                .medicalHistory("Different medical history")
                .build();
        
        assertEquals(pacilian1, pacilian2);
        assertNotEquals(pacilian1, pacilian3);
        assertEquals(pacilian1.hashCode(), pacilian2.hashCode());
        assertNotEquals(pacilian1.hashCode(), pacilian3.hashCode());
    }
}