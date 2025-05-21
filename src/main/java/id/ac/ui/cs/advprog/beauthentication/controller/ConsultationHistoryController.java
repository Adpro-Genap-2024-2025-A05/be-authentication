package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.ConsultationHistoryDto;
import id.ac.ui.cs.advprog.beauthentication.service.ConsultationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultation-history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsultationHistoryController {

    private final ConsultationHistoryService consultationHistoryService;

    @GetMapping
    public List<ConsultationHistoryDto> getConsultationHistory(Authentication authentication) {
        return consultationHistoryService.getConsultationHistory(authentication);
    }
}