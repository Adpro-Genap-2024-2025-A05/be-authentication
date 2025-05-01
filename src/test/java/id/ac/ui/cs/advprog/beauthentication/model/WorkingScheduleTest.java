package id.ac.ui.cs.advprog.beauthentication.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class WorkingScheduleTest {

    @Nested
    class LifecycleTests {
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
    }

    @Nested
    class BuilderTests {
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
    }

    @Nested
    class GetterSetterTests {
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
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForSameId() {
            WorkingSchedule schedule1 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            WorkingSchedule schedule2 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            
            assertEquals(schedule1, schedule2);
        }

        @Test
        void equalsReturnsFalseForDifferentId() {
            WorkingSchedule schedule1 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            WorkingSchedule schedule2 = createSchedule("different-id", DayOfWeek.MONDAY);
            
            assertNotEquals(schedule1, schedule2);
        }

        @Test
        void hashCodeReturnsSameValueForEqualObjects() {
            WorkingSchedule schedule1 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            WorkingSchedule schedule2 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            
            assertEquals(schedule1.hashCode(), schedule2.hashCode());
        }
        
        @Test
        void hashCodeReturnsDifferentValueForDifferentObjects() {
            WorkingSchedule schedule1 = createSchedule("schedule-id", DayOfWeek.MONDAY);
            WorkingSchedule schedule2 = createSchedule("different-id", DayOfWeek.TUESDAY);
            
            assertNotEquals(schedule1.hashCode(), schedule2.hashCode());
        }
    }
    
    private WorkingSchedule createSchedule(String id, DayOfWeek dayOfWeek) {
        return WorkingSchedule.builder()
                .id(id)
                .dayOfWeek(dayOfWeek)
                .build();
    }
}