package id.ac.ui.cs.advprog.beauthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaregiverPublicDto {
    private String id;
    private String name;
    private String email;
    private String speciality;
    private String workAddress;
    private String phoneNumber;
}