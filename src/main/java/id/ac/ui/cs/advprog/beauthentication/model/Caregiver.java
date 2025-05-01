package id.ac.ui.cs.advprog.beauthentication.model;

import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caregivers")
public class Caregiver extends User {
    @Column(nullable = false)
    private String speciality;

    @Column(name = "work_address", nullable = false)
    private String workAddress;

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkingSchedule> workingSchedules = new ArrayList<>();

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null) {
            setRole(Role.CAREGIVER);
        }
    }

    public void addWorkingSchedule(WorkingSchedule schedule) {
        if (schedule == null) {
            return;
        }
        
        workingSchedules.add(schedule);
        schedule.setCaregiver(this);
    }

    public void removeWorkingSchedule(WorkingSchedule schedule) {
        if (schedule == null || !workingSchedules.contains(schedule)) {
            return;
        }
        
        workingSchedules.remove(schedule);
        schedule.setCaregiver(null);
    }
}