package id.ac.ui.cs.advprog.beauthentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_choices")
public class TimeChoice {
    @Id
    @Column(nullable = false, updatable = false, unique = true, length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "working_schedule_id")
    private WorkingSchedule workingSchedule;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}