package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class WorkingScheduleDtoTest {

    private static final DayOfWeek TEST_DAY = DayOfWeek.MONDAY;
    private static final DayOfWeek DIFFERENT_DAY = DayOfWeek.TUESDAY;

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesWorkingScheduleDto() {
            WorkingScheduleDto dto = createDto(TEST_DAY);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
        }

        @Test
        void noArgsConstructorCreatesEmptyWorkingScheduleDto() {
            WorkingScheduleDto dto = new WorkingScheduleDto();
            
            assertNull(dto.getDayOfWeek());
        }

        @Test
        void allArgsConstructorCreatesWorkingScheduleDto() {
            WorkingScheduleDto dto = new WorkingScheduleDto(TEST_DAY);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            WorkingScheduleDto dto = new WorkingScheduleDto();
            
            dto.setDayOfWeek(TEST_DAY);
            
            assertEquals(TEST_DAY, dto.getDayOfWeek());
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
        void equalsReturnsFalseForDifferentObjects() {
            WorkingScheduleDto dto1 = createDto(TEST_DAY);
            WorkingScheduleDto dto2 = createDto(DIFFERENT_DAY);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private WorkingScheduleDto createDto(DayOfWeek dayOfWeek) {
        return WorkingScheduleDto.builder()
                .dayOfWeek(dayOfWeek)
                .build();
    }
}