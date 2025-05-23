package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.CaregiverPublicDto;
import id.ac.ui.cs.advprog.beauthentication.dto.PacilianPublicDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;

    public List<CaregiverPublicDto> getAllActiveCaregivers() {
        return caregiverRepository.findAll()
                .stream()
                .map(this::convertToPublicDtoCaregiver)
                .collect(Collectors.toList());
    }

    public List<CaregiverPublicDto> searchCaregivers(String name, Speciality speciality) {
        List<Caregiver> caregivers = caregiverRepository.findAll();
        
        return caregivers.stream()
                .filter(caregiver -> matchesSearchCriteria(caregiver, name, speciality))
                .map(this::convertToPublicDtoCaregiver)
                .collect(Collectors.toList());
    }

    public CaregiverPublicDto getCaregiverById(String id) {
        Caregiver caregiver = caregiverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caregiver not found with id: " + id));
        
        return convertToPublicDtoCaregiver(caregiver);
    }

    public PacilianPublicDto getPacilianById(String id) {
        Pacilian pacilian = pacilianRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pacilian not found with id: " + id));
        
        return convertToPublicDtoPacilian(pacilian);
    }

    private boolean matchesSearchCriteria(Caregiver caregiver, String name, Speciality speciality) {
        boolean nameMatches = name == null || name.trim().isEmpty() || 
                caregiver.getName().toLowerCase().contains(name.toLowerCase());
        
        boolean specialityMatches = speciality == null || 
                caregiver.getSpeciality().equals(speciality);
        
        return nameMatches && specialityMatches;
    }

    private CaregiverPublicDto convertToPublicDtoCaregiver(Caregiver caregiver) {
        return CaregiverPublicDto.builder()
                .id(caregiver.getId())
                .name(caregiver.getName())
                .email(caregiver.getEmail())
                .speciality(caregiver.getSpeciality())
                .workAddress(caregiver.getWorkAddress())
                .phoneNumber(caregiver.getPhoneNumber())
                .build();
    }

    private PacilianPublicDto convertToPublicDtoPacilian(Pacilian pacilian) {
        return PacilianPublicDto.builder()
                .id(pacilian.getId())
                .name(pacilian.getName())
                .email(pacilian.getEmail())
                .phoneNumber(pacilian.getPhoneNumber())
                .build();
    }
}