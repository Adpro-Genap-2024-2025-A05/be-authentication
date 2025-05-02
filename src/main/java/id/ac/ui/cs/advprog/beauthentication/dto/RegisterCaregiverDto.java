package id.ac.ui.cs.advprog.beauthentication.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCaregiverDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "NIK is required")
    @Pattern(regexp = "\\d{16}", message = "NIK must be 16 digits")
    private String nik;

    @NotBlank(message = "Work address is required")
    private String workAddress;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,13}", message = "Phone number must be 10-13 digits")
    private String phoneNumber;

    @NotBlank(message = "Speciality is required")
    private String speciality;

    @NotEmpty(message = "Working schedules are required")
    @Valid
    private List<WorkingScheduleDto> workingSchedules;
}