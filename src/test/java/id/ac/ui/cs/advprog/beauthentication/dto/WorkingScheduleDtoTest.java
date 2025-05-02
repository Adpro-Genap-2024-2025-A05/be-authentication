package id.ac.ui.cs.advprog.beauthentication.dto;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class WorkingScheduleDtoTest {

    @Test
    void builderCreatesWorkingScheduleDto() {
        WorkingScheduleDto dto = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        assertEquals(DayOfWeek.MONDAY, dto.getDayOfWeek());
    }

    @Test
    void noArgsConstructorCreatesEmptyWorkingScheduleDto() {
        WorkingScheduleDto dto = new WorkingScheduleDto();
        assertNull(dto.getDayOfWeek());
    }

    @Test
    void allArgsConstructorCreatesWorkingScheduleDto() {
        WorkingScheduleDto dto = new WorkingScheduleDto(DayOfWeek.MONDAY);
        assertEquals(DayOfWeek.MONDAY, dto.getDayOfWeek());
    }

    @Test
    void gettersAndSettersWorkCorrectly() {
        WorkingScheduleDto dto = new WorkingScheduleDto();
        dto.setDayOfWeek(DayOfWeek.MONDAY);
        assertEquals(DayOfWeek.MONDAY, dto.getDayOfWeek());
    }

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        WorkingScheduleDto dto1 = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        WorkingScheduleDto dto2 = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .build();
        
        WorkingScheduleDto dto3 = WorkingScheduleDto.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .build();
        
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }
}