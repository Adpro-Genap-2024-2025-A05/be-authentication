package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkingScheduleDtoTest {

    private static final DayOfWeek TEST_DAY = DayOfWeek.MONDAY;
    private static final DayOfWeek DIFFERENT_DAY = DayOfWeek.TUESDAY;
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesWorkingScheduleDto() {
            WorkingScheduleDto dto = createDto(TEST_DAY);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
            assertNotNull(dto.getTimeChoices());
            assertEquals(1, dto.getTimeChoices().size());
            assertEquals(START_TIME, dto.getTimeChoices().get(0).getStartTime());
            assertEquals(END_TIME, dto.getTimeChoices().get(0).getEndTime());
        }

        @Test
        void noArgsConstructorCreatesEmptyWorkingScheduleDto() {
            WorkingScheduleDto dto = new WorkingScheduleDto();
            
            assertNull(dto.getDayOfWeek());
            assertNull(dto.getTimeChoices());
        }

        @Test
        void allArgsConstructorCreatesWorkingScheduleDto() {
            List<TimeChoiceDto> timeChoices = createTimeChoices();
            WorkingScheduleDto dto = new WorkingScheduleDto(TEST_DAY, timeChoices);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
            assertEquals(timeChoices, dto.getTimeChoices());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            WorkingScheduleDto dto = new WorkingScheduleDto();
            List<TimeChoiceDto> timeChoices = createTimeChoices();
            
            dto.setDayOfWeek(TEST_DAY);
            dto.setTimeChoices(timeChoices);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
            assertEquals(timeChoices, dto.getTimeChoices());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            WorkingScheduleDto dto1 = createDto(TEST_DAY);
            WorkingScheduleDto dto2 = createDto(TEST_DAY);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentDayOfWeek() {
            WorkingScheduleDto dto1 = createDto(TEST_DAY);
            WorkingScheduleDto dto2 = createDto(DIFFERENT_DAY);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentTimeChoices() {
            WorkingScheduleDto dto1 = createDto(TEST_DAY);
            
            List<TimeChoiceDto> differentTimeChoices = new ArrayList<>();
            differentTimeChoices.add(TimeChoiceDto.builder()
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(16, 0))
                    .build());
            
            WorkingScheduleDto dto2 = WorkingScheduleDto.builder()
                    .dayOfWeek(TEST_DAY)
                    .timeChoices(differentTimeChoices)
                    .build();
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private List<TimeChoiceDto> createTimeChoices() {
        List<TimeChoiceDto> timeChoices = new ArrayList<>();
        timeChoices.add(TimeChoiceDto.builder()
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        return timeChoices;
    }
    
    private WorkingScheduleDto createDto(DayOfWeek dayOfWeek) {
        return WorkingScheduleDto.builder()
                .dayOfWeek(dayOfWeek)
                .timeChoices(createTimeChoices())
                .build();
    }
}