package id.ac.ui.cs.advprog.beauthentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {
    @Email(message = "Invalid email format")
    private String email;
    
    private String name;
    
    private String address;
    
    @Pattern(regexp = "\\d{10,13}", message = "Phone number must be 10-13 digits")
    private String phoneNumber;
    
    private String medicalHistory;
    
    private String speciality;
    private String workAddress;
}