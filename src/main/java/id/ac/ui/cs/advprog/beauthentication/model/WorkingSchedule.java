package id.ac.ui.cs.advprog.beauthentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "working_schedules")
public class WorkingSchedule {
    @Id
    @Column(nullable = false, updatable = false, unique = true, length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "workingSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimeChoice> timeChoices = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    public void addTimeChoice(TimeChoice timeChoice) {
        if (timeChoice == null) {
            return;
        }
        
        timeChoices.add(timeChoice);
        timeChoice.setWorkingSchedule(this);
    }

    public void removeTimeChoice(TimeChoice timeChoice) {
        if (timeChoice == null || !timeChoices.contains(timeChoice)) {
            return;
        }
        
        timeChoices.remove(timeChoice);
        timeChoice.setWorkingSchedule(null);
    }
}