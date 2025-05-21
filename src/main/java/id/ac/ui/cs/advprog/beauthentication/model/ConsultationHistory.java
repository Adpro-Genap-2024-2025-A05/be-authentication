package id.ac.ui.cs.advprog.beauthentication.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consultation_histories")
public class ConsultationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "pacilian_id")
    private Pacilian pacilian;

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    private LocalDateTime consultationTime;

    @Column(columnDefinition = "TEXT")
    private String note;
}