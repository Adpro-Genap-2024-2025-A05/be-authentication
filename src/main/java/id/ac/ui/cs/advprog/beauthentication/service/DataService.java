package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.CaregiverPublicDto;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {

    private final CaregiverRepository caregiverRepository;

    public List<CaregiverPublicDto> getAllActiveCaregivers() {
        return caregiverRepository.findAll()
                .stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    public List<CaregiverPublicDto> searchCaregivers(String name, String speciality) {
        List<Caregiver> caregivers = caregiverRepository.findAll();
        
        return caregivers.stream()
                .filter(caregiver -> matchesSearchCriteria(caregiver, name, speciality))
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    public CaregiverPublicDto getCaregiverById(String id) {
        Caregiver caregiver = caregiverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found with id: " + id));
        
        return convertToPublicDto(caregiver);
    }

    private boolean matchesSearchCriteria(Caregiver caregiver, String name, String speciality) {
        boolean nameMatches = name == null || name.trim().isEmpty() || 
                caregiver.getName().toLowerCase().contains(name.toLowerCase());
        
        boolean specialityMatches = speciality == null || speciality.trim().isEmpty() || 
                caregiver.getSpeciality().toLowerCase().contains(speciality.toLowerCase());
        
        return nameMatches && specialityMatches;
    }

    private CaregiverPublicDto convertToPublicDto(Caregiver caregiver) {
        return CaregiverPublicDto.builder()
                .id(caregiver.getId())
                .name(caregiver.getName())
                .email(caregiver.getEmail())
                .speciality(caregiver.getSpeciality())
                .workAddress(caregiver.getWorkAddress())
                .phoneNumber(caregiver.getPhoneNumber())
                .build();
    }
}