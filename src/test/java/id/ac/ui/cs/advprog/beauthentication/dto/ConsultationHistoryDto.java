package id.ac.ui.cs.advprog.beauthentication.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationHistoryDto {
    private LocalDateTime consultationTime;
    private String partnerName;
    private String partnerRole;
    private String note;
}