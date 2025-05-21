package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.ConsultationHistoryDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Role;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.model.User;
import id.ac.ui.cs.advprog.beauthentication.repository.ConsultationHistoryRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationHistoryServiceImpl implements ConsultationHistoryService {

    private final ConsultationHistoryRepository consultationHistoryRepository;
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    public List<ConsultationHistoryDto> getConsultationHistory(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole() == Role.PACILIAN) {
            Pacilian pacilian = pacilianRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacilian not found"));
            return consultationHistoryRepository.findByPacilian(pacilian)
                    .stream()
                    .map(ch -> ConsultationHistoryDto.builder()
                            .consultationTime(ch.getConsultationTime())
                            .partnerName(ch.getCaregiver().getName())
                            .partnerRole("CAREGIVER")
                            .note(ch.getNote())
                            .build())
                    .collect(Collectors.toList());
        } else if (user.getRole() == Role.CAREGIVER) {
            Caregiver caregiver = caregiverRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("Caregiver not found"));
            return consultationHistoryRepository.findByCaregiver(caregiver)
                    .stream()
                    .map(ch -> ConsultationHistoryDto.builder()
                            .consultationTime(ch.getConsultationTime())
                            .partnerName(ch.getPacilian().getName())
                            .partnerRole("PACILIAN")
                            .note(ch.getNote())
                            .build())
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}