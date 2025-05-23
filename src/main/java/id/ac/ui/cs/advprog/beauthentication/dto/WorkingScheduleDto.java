package id.ac.ui.cs.advprog.beauthentication.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkingScheduleDto {
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotEmpty(message = "Time choices are required")
    @Valid
    private List<TimeChoiceDto> timeChoices;
}