package id.ac.ui.cs.advprog.beauthentication.controller;

import id.ac.ui.cs.advprog.beauthentication.dto.ApiResponseDto;
import id.ac.ui.cs.advprog.beauthentication.dto.CaregiverPublicDto;
import id.ac.ui.cs.advprog.beauthentication.dto.PacilianPublicDto;
import id.ac.ui.cs.advprog.beauthentication.enums.Speciality;
import id.ac.ui.cs.advprog.beauthentication.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
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

                String decodedSpeciality = speciality;
                if (speciality != null && !speciality.trim().isEmpty()) {
                        try {
                                decodedSpeciality = java.net.URLDecoder.decode(speciality, "UTF-8");

                                if (decodedSpeciality.contains("%")) {
                                        decodedSpeciality = java.net.URLDecoder.decode(decodedSpeciality, "UTF-8");
                                }
                        } catch (Exception e) {
                                decodedSpeciality = speciality;
                        }
                }

                Speciality specialityEnum = null;
                if (decodedSpeciality != null && !decodedSpeciality.trim().isEmpty()) {
                        try {
                                specialityEnum = Speciality.fromDisplayName(decodedSpeciality.trim());
                        } catch (IllegalArgumentException e) {
                                return ResponseEntity.badRequest().body(
                                                ApiResponseDto.error(HttpStatus.BAD_REQUEST.value(),
                                                                "Invalid speciality: " + decodedSpeciality +
                                                                                ". Valid specialities are: "
                                                                                + java.util.Arrays.toString(
                                                                                                Speciality.values())));
                        }
                }

                List<CaregiverPublicDto> caregivers = dataService.searchCaregivers(name, specialityEnum);
                return ResponseEntity.ok(
                                ApiResponseDto.success(HttpStatus.OK.value(),
                                                "Caregivers search completed successfully",
                                                caregivers));
        }

        @GetMapping(path = "/caregiver/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ApiResponseDto<CaregiverPublicDto>> getCaregiverById(@PathVariable String id) {
                CaregiverPublicDto caregiver = dataService.getCaregiverById(id);
                return ResponseEntity.ok(
                                ApiResponseDto.success(HttpStatus.OK.value(),
                                                "Caregiver retrieved successfully",
                                                caregiver));
        }

        @GetMapping(path = "/pacilian/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ApiResponseDto<PacilianPublicDto>> getPacilianById(@PathVariable String id) {
                PacilianPublicDto pacilian = dataService.getPacilianById(id);
                return ResponseEntity.ok(
                                ApiResponseDto.success(HttpStatus.OK.value(),
                                                "Pacilian retrieved successfully",
                                                pacilian));
        }
}