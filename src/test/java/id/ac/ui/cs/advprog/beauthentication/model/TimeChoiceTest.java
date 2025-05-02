package id.ac.ui.cs.advprog.beauthentication.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeChoiceTest {

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);

    @Nested
    class LifecycleTests {
        @Test
        void onCreateGeneratesUUID() {
            TimeChoice timeChoice = new TimeChoice();

            timeChoice.onCreate();

            assertNotNull(timeChoice.getId());
            assertEquals(36, timeChoice.getId().length());
        }

        @Test
        void onCreateDoesNotChangeExistingId() {
            TimeChoice timeChoice = new TimeChoice();
            String initialId = "existing-id-value";
            timeChoice.setId(initialId);

            timeChoice.onCreate();

            assertEquals(initialId, timeChoice.getId());
        }
    }

    @Nested
    class BuilderTests {
        @Test
        void builderCreatesTimeChoice() {
            TimeChoice timeChoice = TimeChoice.builder()
                    .id("time-choice-id")
                    .startTime(START_TIME)
                    .endTime(END_TIME)
                    .build();

            assertEquals("time-choice-id", timeChoice.getId());
            assertEquals(START_TIME, timeChoice.getStartTime());
            assertEquals(END_TIME, timeChoice.getEndTime());
            assertNull(timeChoice.getWorkingSchedule());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            TimeChoice timeChoice = new TimeChoice();
            WorkingSchedule schedule = new WorkingSchedule();

            timeChoice.setId("time-choice-id");
            timeChoice.setWorkingSchedule(schedule);
            timeChoice.setStartTime(START_TIME);
            timeChoice.setEndTime(END_TIME);

            assertEquals("time-choice-id", timeChoice.getId());
            assertEquals(schedule, timeChoice.getWorkingSchedule());
            assertEquals(START_TIME, timeChoice.getStartTime());
            assertEquals(END_TIME, timeChoice.getEndTime());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForSameId() {
            TimeChoice timeChoice1 = createTimeChoice("time-choice-id", START_TIME, END_TIME);
            TimeChoice timeChoice2 = createTimeChoice("time-choice-id", START_TIME, END_TIME);

            assertEquals(timeChoice1, timeChoice2);
        }

        @Test
        void equalsReturnsFalseForDifferentId() {
            TimeChoice timeChoice1 = createTimeChoice("time-choice-id", START_TIME, END_TIME);
            TimeChoice timeChoice2 = createTimeChoice("different-id", START_TIME, END_TIME);

            assertNotEquals(timeChoice1, timeChoice2);
        }

        @Test
        void hashCodeReturnsSameValueForEqualObjects() {
            TimeChoice timeChoice1 = createTimeChoice("time-choice-id", START_TIME, END_TIME);
            TimeChoice timeChoice2 = createTimeChoice("time-choice-id", START_TIME, END_TIME);

            assertEquals(timeChoice1.hashCode(), timeChoice2.hashCode());
        }

        @Test
        void hashCodeReturnsDifferentValueForDifferentObjects() {
            TimeChoice timeChoice1 = createTimeChoice("time-choice-id", START_TIME, END_TIME);
            TimeChoice timeChoice2 = createTimeChoice("different-id", LocalTime.of(10, 0), LocalTime.of(16, 0));

            assertNotEquals(timeChoice1.hashCode(), timeChoice2.hashCode());
        }
    }

    @Nested
    class ValidationTests {
        @Test
        void startTimeBeforeEndTimeIsValid() {
            TimeChoice timeChoice = createTimeChoice("id", LocalTime.of(9, 0), LocalTime.of(17, 0));
            assertNotNull(timeChoice);
        }
    }

    private TimeChoice createTimeChoice(String id, LocalTime startTime, LocalTime endTime) {
        return TimeChoice.builder()
                .id(id)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}