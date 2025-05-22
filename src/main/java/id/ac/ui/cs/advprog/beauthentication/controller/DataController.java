package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beauthentication.dto.CaregiverPublicDto;
import id.ac.ui.cs.advprog.beauthentication.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DataController {

    private final DataService dataService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CaregiverPublicDto>>> getAllCaregivers() {
        List<CaregiverPublicDto> caregivers = dataService.getAllActiveCaregivers();
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                        "Caregivers retrieved successfully",
                        caregivers));
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CaregiverPublicDto>>> searchCaregivers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String speciality) {
        
        List<CaregiverPublicDto> caregivers = dataService.searchCaregivers(name, speciality);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                        "Caregivers search completed successfully",
                        caregivers));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CaregiverPublicDto>> getCaregiverById(@PathVariable String id) {
        CaregiverPublicDto caregiver = dataService.getCaregiverById(id);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(),
                        "Caregiver retrieved successfully",
                        caregiver));
    }
}