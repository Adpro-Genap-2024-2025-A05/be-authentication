package id.ac.ui.cs.advprog.beauthentication.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class WorkingScheduleTest {

    @Test
    void onCreateGeneratesUUID() {
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.onCreate();
        assertNotNull(schedule.getId());
        assertEquals(36, schedule.getId().length());
    }

    @Test
    void onCreateDoesNotChangeExistingId() {
        WorkingSchedule schedule = new WorkingSchedule();
        String initialId = "existing-id-value";
        schedule.setId(initialId);

        schedule.onCreate();
        assertEquals(initialId, schedule.getId());
    }

    @Test
    void builderCreatesWorkingSchedule() {
        
        WorkingSchedule schedule = WorkingSchedule.builder()
                .id("schedule-id")
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        assertEquals("schedule-id", schedule.getId());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertNull(schedule.getCaregiver());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        WorkingSchedule schedule = new WorkingSchedule();
        Caregiver caregiver = new Caregiver();
        
        schedule.setId("schedule-id");
        schedule.setCaregiver(caregiver);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        assertEquals("schedule-id", schedule.getId());
        assertEquals(caregiver, schedule.getCaregiver());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        
        WorkingSchedule schedule1 = WorkingSchedule.builder()
                .id("schedule-id")
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        WorkingSchedule schedule2 = WorkingSchedule.builder()
                .id("schedule-id")
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        WorkingSchedule schedule3 = WorkingSchedule.builder()
                .id("different-id")
                .dayOfWeek(DayOfWeek.TUESDAY)
                .build();
        
        assertEquals(schedule1, schedule2);
        assertNotEquals(schedule1, schedule3);
        assertEquals(schedule1.hashCode(), schedule2.hashCode());
        assertNotEquals(schedule1.hashCode(), schedule3.hashCode());
    }
}