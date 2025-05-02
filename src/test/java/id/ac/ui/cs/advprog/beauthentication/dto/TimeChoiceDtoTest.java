package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeChoiceDtoTest {

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(17, 0);
    private static final LocalTime DIFFERENT_START_TIME = LocalTime.of(10, 0);
    private static final LocalTime DIFFERENT_END_TIME = LocalTime.of(16, 0);

    @Nested
    class ConstructorTests {
        @Test
        void builderCreatesTimeChoiceDto() {
            TimeChoiceDto dto = createDto(START_TIME, END_TIME);
            
            assertEquals(START_TIME, dto.getStartTime());
            assertEquals(END_TIME, dto.getEndTime());
        }

        @Test
        void noArgsConstructorCreatesEmptyTimeChoiceDto() {
            TimeChoiceDto dto = new TimeChoiceDto();
            
            assertNull(dto.getStartTime());
            assertNull(dto.getEndTime());
        }

        @Test
        void allArgsConstructorCreatesTimeChoiceDto() {
            TimeChoiceDto dto = new TimeChoiceDto(START_TIME, END_TIME);
            
            assertEquals(START_TIME, dto.getStartTime());
            assertEquals(END_TIME, dto.getEndTime());
        }
    }

    @Nested
    class GetterSetterTests {
        @Test
        void gettersAndSettersWorkCorrectly() {
            TimeChoiceDto dto = new TimeChoiceDto();
            
            dto.setStartTime(START_TIME);
            dto.setEndTime(END_TIME);
            
            assertEquals(START_TIME, dto.getStartTime());
            assertEquals(END_TIME, dto.getEndTime());
        }
    }

    @Nested
    class EqualsHashCodeTests {
        @Test
        void equalsReturnsTrueForEquivalentObjects() {
            TimeChoiceDto dto1 = createDto(START_TIME, END_TIME);
            TimeChoiceDto dto2 = createDto(START_TIME, END_TIME);
            
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentStartTime() {
            TimeChoiceDto dto1 = createDto(START_TIME, END_TIME);
            TimeChoiceDto dto2 = createDto(DIFFERENT_START_TIME, END_TIME);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
        
        @Test
        void equalsReturnsFalseForDifferentEndTime() {
            TimeChoiceDto dto1 = createDto(START_TIME, END_TIME);
            TimeChoiceDto dto2 = createDto(START_TIME, DIFFERENT_END_TIME);
            
            assertNotEquals(dto1, dto2);
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }
    
    private TimeChoiceDto createDto(LocalTime startTime, LocalTime endTime) {
        return TimeChoiceDto.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}