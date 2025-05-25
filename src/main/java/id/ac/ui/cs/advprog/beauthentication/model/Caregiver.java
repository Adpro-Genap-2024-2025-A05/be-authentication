package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caregivers")
public class Caregiver extends User {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Speciality speciality;

    @Column(name = "work_address", nullable = false)
    private String workAddress;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null) {
            setRole(Role.CAREGIVER);
        }
    }
}