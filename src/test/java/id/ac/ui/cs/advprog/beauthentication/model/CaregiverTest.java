package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverTest {

    @Test
    void builderCreatesCaregiver() {
        Caregiver caregiver = Caregiver.builder()
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
                .workingSchedules(new ArrayList<>())
                .build();
        
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
        assertNotNull(caregiver.getWorkingSchedules());
        assertTrue(caregiver.getWorkingSchedules().isEmpty());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        Caregiver caregiver = new Caregiver();
        
        caregiver.setSpeciality("Neurology");
        caregiver.setWorkAddress("New Work Address");
        
        assertEquals("Neurology", caregiver.getSpeciality());
        assertEquals("New Work Address", caregiver.getWorkAddress());
    }

    @Test
    void inheritanceExtendedFromUser() {
        Caregiver caregiver = new Caregiver();
        assertInstanceOf(User.class, caregiver);
    }

    @Test
    void workingSchedulesDefaultToEmptyList() {
        Caregiver caregiver = new Caregiver();
        assertNotNull(caregiver.getWorkingSchedules());
        assertTrue(caregiver.getWorkingSchedules().isEmpty());
    }

    @Test
    void addWorkingScheduleAddsScheduleAndSetsCaregiver() {
        Caregiver caregiver = Caregiver.builder()
                .workingSchedules(new ArrayList<>())
                .build();
        
        WorkingSchedule schedule = WorkingSchedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        caregiver.addWorkingSchedule(schedule);
        
        assertEquals(1, caregiver.getWorkingSchedules().size());
        assertEquals(schedule, caregiver.getWorkingSchedules().get(0));
        assertEquals(caregiver, schedule.getCaregiver());
    }

    @Test
    void removeWorkingScheduleRemovesScheduleAndNullsCaregiver() {
        Caregiver caregiver = Caregiver.builder()
                .workingSchedules(new ArrayList<>())
                .build();
        
        WorkingSchedule schedule = WorkingSchedule.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        caregiver.addWorkingSchedule(schedule);
        
        caregiver.removeWorkingSchedule(schedule);
        
        assertTrue(caregiver.getWorkingSchedules().isEmpty());
        assertNull(schedule.getCaregiver());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        Caregiver caregiver1 = Caregiver.builder()
                .id("caregiver-id")
                .email("caregiver@example.com")
                .speciality("Cardiology")
                .workAddress("Work Address")
                .build();
        
        Caregiver caregiver2 = Caregiver.builder()
                .id("caregiver-id")
                .email("caregiver@example.com")
                .speciality("Cardiology")
                .workAddress("Work Address")
                .build();
        
        Caregiver caregiver3 = Caregiver.builder()
                .id("different-id")
                .email("different@example.com")
                .speciality("Neurology")
                .workAddress("Different Address")
                .build();
        
        assertEquals(caregiver1, caregiver2);
        assertNotEquals(caregiver1, caregiver3);
        assertEquals(caregiver1.hashCode(), caregiver2.hashCode());
        assertNotEquals(caregiver1.hashCode(), caregiver3.hashCode());
    }
}