package id.ac.ui.cs.advprog.beauthentication.dto;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String id;
    private String email;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;
    private Role role;
    private String medicalHistory;
    private String speciality;
    private String workAddress;
}