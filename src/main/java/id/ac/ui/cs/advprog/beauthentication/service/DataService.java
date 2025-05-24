package id.ac.ui.cs.advprog.beauthentication.service;

import id.ac.ui.cs.advprog.beauthentication.dto.CaregiverPublicDto;
import id.ac.ui.cs.advprog.beauthentication.dto.PacilianPublicDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import id.ac.ui.cs.advprog.beauthentication.model.Caregiver;
import id.ac.ui.cs.advprog.beauthentication.model.Pacilian;
import id.ac.ui.cs.advprog.beauthentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.beauthentication.repository.PacilianRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {
    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    
    private final Counter dataRequestCounter;
    private final Counter caregiverSearchCounter;
    private final Counter caregiverViewCounter;
    private final Counter pacilianViewCounter;
    private final Timer dataQueryTimer;
    
    private final Counter dataRequestFailureCounter;
    private final Counter caregiverNotFoundCounter;
    private final Counter pacilianNotFoundCounter;

    public List<CaregiverPublicDto> getAllActiveCaregivers() {
        try {
            return dataQueryTimer.recordCallable(() -> {
                dataRequestCounter.increment();
                
                return caregiverRepository.findAll()
                        .stream()
                        .map(this::convertToPublicDtoCaregiver)
                        .collect(Collectors.toList());
            });
        } catch (Exception e) {
            dataRequestFailureCounter.increment();
            throw new RuntimeException(e);
        }
    }

    public List<CaregiverPublicDto> searchCaregivers(String name, Speciality speciality) {
        try {
            return dataQueryTimer.recordCallable(() -> {
                caregiverSearchCounter.increment();
                dataRequestCounter.increment();

                List<Caregiver> caregivers = caregiverRepository.findAll();

                return caregivers.stream()
                        .filter(caregiver -> matchesSearchCriteria(caregiver, name, speciality))
                        .map(this::convertToPublicDtoCaregiver)
                        .collect(Collectors.toList());
            });
        } catch (Exception e) {
            dataRequestFailureCounter.increment();
            throw new RuntimeException(e);
        }
    }

    public CaregiverPublicDto getCaregiverById(String id) {
        try {
            return dataQueryTimer.recordCallable(() -> {
                caregiverViewCounter.increment();
                dataRequestCounter.increment();
                
                Caregiver caregiver = caregiverRepository.findById(id)
                        .orElseThrow(() -> {
                            caregiverNotFoundCounter.increment();
                            return new IllegalArgumentException("Caregiver not found with id: " + id);
                        });
                
                return convertToPublicDtoCaregiver(caregiver);
            });
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            dataRequestFailureCounter.increment();
            throw new RuntimeException(e);
        }
    }

    public PacilianPublicDto getPacilianById(String id) {
        try {
            return dataQueryTimer.recordCallable(() -> {
                pacilianViewCounter.increment();
                dataRequestCounter.increment();
                
                Pacilian pacilian = pacilianRepository.findById(id)
                        .orElseThrow(() -> {
                            pacilianNotFoundCounter.increment();
                            return new IllegalArgumentException("Pacilian not found with id: " + id);
                        });
                
                return convertToPublicDtoPacilian(pacilian);
            });
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            dataRequestFailureCounter.increment();
            throw new RuntimeException(e);
        }
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