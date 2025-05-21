package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.ConsultationHistoryDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ConsultationHistoryService {
    List<ConsultationHistoryDto> getConsultationHistory(Authentication authentication);
}